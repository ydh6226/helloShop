package com.ydh.helloshop.application.controller.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ydh.helloshop.application.controller.order.dto.RequestOrderParam;
import com.ydh.helloshop.application.controller.order.dto.ResponseOrderInfo;
import com.ydh.helloshop.application.controller.order.dto.ResponseOrderParam;
import com.ydh.helloshop.application.domain.delivery.Delivery;
import com.ydh.helloshop.application.domain.delivery.DeliveryStatus;
import com.ydh.helloshop.application.domain.item.Item;
import com.ydh.helloshop.application.domain.member.CurrentMember;
import com.ydh.helloshop.application.domain.member.Member;
import com.ydh.helloshop.application.domain.order.Order;
import com.ydh.helloshop.application.exception.ItemException;
import com.ydh.helloshop.application.repository.item.ItemRepository;
import com.ydh.helloshop.application.repository.order.OrderSearch;
import com.ydh.helloshop.application.service.OrderService;
import com.ydh.helloshop.infra.amqp.dto.DeliveryPublishParam;
import com.ydh.helloshop.infra.amqp.sender.DeliveryPublisher;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ServerErrorException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final ItemRepository itemRepository;

    private final DeliveryPublisher deliveryPublisher;

    private final ObjectMapper objectMapper;

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
    public String orderList(Model model, @CurrentMember Member member) {
        List<Order> orders = orderService.findAll(new OrderSearch(member.getId()));
        List<OrderListDto> orderListDto = new ArrayList<>();

        orders.forEach(o -> {
            List<OrderListDto> result = o.getOrderItems()
                    .stream()
                    .map(oi -> new OrderListDto(
                            oi.getItem().getId(),
                            oi.getItem().getName(), oi.getOrder().getOrderDate(),
                            oi.getTotalPrice(), oi.getDelivery().getStatus())).collect(Collectors.toList());
            orderListDto.addAll(result);
        });

        model.addAttribute("orderListDto", orderListDto);

        return "order/orderList";
    }

    @Data
    static class OrderInfoDto {
        int count;
        Long itemId;
    }

    @Data
    static class OrderListDto {
        private Long itemId;
        private String itemName;
        String orderDate;
        private int price;
        private DeliveryStatus deliveryStatus;

        public OrderListDto(Long itemId, String itemName, LocalDateTime orderDate, int price, DeliveryStatus deliveryStatus) {
            this.itemId = itemId;
            this.itemName = itemName;
            this.price = price;
            this.deliveryStatus = deliveryStatus;

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            this.orderDate = orderDate.format(formatter);
        }
    }
}
