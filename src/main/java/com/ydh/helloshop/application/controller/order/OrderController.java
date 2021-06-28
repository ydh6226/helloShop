package com.ydh.helloshop.application.controller.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ydh.helloshop.application.controller.order.dto.CreateOrderParam;
import com.ydh.helloshop.application.controller.order.dto.RequestOrderParam;
import com.ydh.helloshop.application.controller.order.dto.ResponseOrderInfo;
import com.ydh.helloshop.application.controller.order.dto.ResponseOrderParam;
import com.ydh.helloshop.application.controller.order.dto.search.OrderParam;
import com.ydh.helloshop.application.controller.order.dto.search.OrderSearchResParam;
import com.ydh.helloshop.application.controller.order.dto.search.PageMetaData;
import com.ydh.helloshop.application.domain.delivery.Delivery;
import com.ydh.helloshop.application.domain.item.Item;
import com.ydh.helloshop.application.domain.member.CurrentMember;
import com.ydh.helloshop.application.domain.member.Member;
import com.ydh.helloshop.application.domain.order.Order;
import com.ydh.helloshop.application.exception.ItemException;
import com.ydh.helloshop.application.repository.item.ItemRepository;
import com.ydh.helloshop.application.repository.order.OrderRepository;
import com.ydh.helloshop.application.repository.order.OrderSearch;
import com.ydh.helloshop.application.service.OrderService;
import com.ydh.helloshop.infra.amqp.dto.DeliveryPublishParam;
import com.ydh.helloshop.infra.amqp.sender.DeliveryPublisher;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;

    private final DeliveryPublisher deliveryPublisher;
    private final ObjectMapper objectMapper;

    @GetMapping(value = "/orders/{id}")
    public String CartCheckView(@PathVariable("id") Long orderId, @CurrentMember Member member, Model model) {
        Order order = orderRepository.findOrderWithOrderItemsAndMember(orderId);
        if (order == null) {
            throw new IllegalArgumentException("잘못된 주문 정보입니다.");
        }
        if (!order.getMember().equals(member)) {
            throw new AccessDeniedException("해당주문은 접근할 수 없습니다.");
        }

        ResponseOrderParam responseOrderParam = new ResponseOrderParam();
        order.getOrderItems()
                .forEach(oi -> responseOrderParam
                        .addParam(new ResponseOrderInfo(oi.getCount(), oi.getItem(), oi.getTotalPrice())));

        model.addAttribute("member", member);
        model.addAttribute("orderInfo", responseOrderParam);

        return "order/orderView";
    }

    @GetMapping(value = "/orders")
    public String orderView(String orderInfo, Model model, @CurrentMember Member member) {
        try {
            RequestOrderParam requestOrderParam = objectMapper.readValue(orderInfo, RequestOrderParam.class);
            ResponseOrderParam responseOrderParam = new ResponseOrderParam();

            // TODO: 2021-05-28[양동혁] in쿼리 사용하는지
            requestOrderParam.getRequestOrderInfos()
                    .forEach(requestOrderInfo -> {
                        int count = requestOrderInfo.getCount();
                        Item item = itemRepository.findById(requestOrderInfo.getItemId())
                                .orElseThrow(ItemException::noSuchItemException);
                        int totalPrice = count * item.getPrice();

                        responseOrderParam.addParam(new ResponseOrderInfo(count, item, totalPrice));
                    });
            model.addAttribute("orderInfo", responseOrderParam);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("JSON 처리 에러");
       }
        model.addAttribute("member", member);

        // TODO: 2021-05-28[양동혁] 상품검색 인터셉터 제외 
        return "order/orderView";
    }

    // TODO: 2021-06-01[양동혁] restapi exception으로 분리
    @PostMapping("/orders")
    @ResponseBody
    public ResponseEntity<Long> createOrder(@RequestBody CreateOrderParam createOrderParam, @CurrentMember Member member) {
        Order order = orderService.createOrder(createOrderParam, member.getId());
        return new ResponseEntity<>(order.getId(), HttpStatus.OK);
    }

    @PostMapping("/order")
    @ResponseBody
    public ResponseEntity<String> order(@RequestBody OrderInfoDto orderInfoDto, @CurrentMember Member member) {
        Long orderId = orderService.orderOne(member.getId(), orderInfoDto.getItemId(), orderInfoDto.getCount());

        Order findOrder = orderService.findOneWithDeliveryAndItem(orderId);
        Delivery delivery = findOrder.getOrderItems().get(0).getDelivery();

        DeliveryPublishParam dto = new DeliveryPublishParam(
                delivery.getId(),
                member.getName(),
                findOrder.getOrderItems().get(0).getItem().getName(),
                delivery.getAddress());

        //rabbitMQ send
        try {
            deliveryPublisher.send(List.of(dto));
            return new ResponseEntity<>("ok", HttpStatus.ACCEPTED);
        } catch (Exception e) {
            // publish 실패하면 생성했던 주문 삭제
            orderService.cancelByRabbitMQError(findOrder);
            return new ResponseEntity<>("rabbitMQ send error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/order/view")
    public String orderList(Model model, @CurrentMember Member member,
                            @PageableDefault(size = 7) Pageable pageable) {
        PageImpl<Order> pageableOrders =
                orderService.findPagedOrdersBySearch(new OrderSearch(member.getId()), pageable);

        OrderSearchResParam orderSearchResParam = new OrderSearchResParam();

        PageMetaData pageMetaData = new PageMetaData(pageableOrders.getNumber(), pageableOrders.getSize(),
                pageableOrders.getTotalPages(), pageableOrders.getTotalElements());
        orderSearchResParam.setPageMetaData(pageMetaData);

        List<Order> orders = pageableOrders.getContent();
        orders.forEach(o -> {
            List<OrderParam> orderParams = o.getOrderItems()
                    .stream()
                    .map(oi -> new OrderParam(oi.getItem().getId(),
                            oi.getItem().getName(),
                            o.getOrderDate(),
                            oi.getItem().getPrice(),
                            oi.getDelivery().getStatus()))
                    .collect(Collectors.toList());
            orderSearchResParam.setOrderParams(orderParams);
        });
        model.addAttribute("searchResult", orderSearchResParam);
        return "order/orderList";
    }

    @Data
    static class OrderInfoDto {
        int count;
        Long itemId;
    }
}
