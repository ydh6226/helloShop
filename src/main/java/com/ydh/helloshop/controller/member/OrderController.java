package com.ydh.helloshop.controller.member;

import com.ydh.helloshop.amqp.dto.DeliveryDelegateDto;
import com.ydh.helloshop.amqp.sender.DeliverySender;
import com.ydh.helloshop.domain.delivery.Delivery;
import com.ydh.helloshop.domain.delivery.DeliveryStatus;
import com.ydh.helloshop.domain.member.Member;
import com.ydh.helloshop.domain.order.Order;
import com.ydh.helloshop.repository.order.OrderSearch;
import com.ydh.helloshop.service.OrderService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final DeliverySender deliverySender;

    @PostMapping("/order")
    @ResponseBody
    public ResponseEntity<String> orderOne(@RequestBody OrderInfoDto orderInfoDto, @AuthenticationPrincipal Member member) {
        Long orderId = orderService.orderOne(member.getId(), orderInfoDto.getItemId(), orderInfoDto.getCount());

        Order findOrder = orderService.findOneWithDeliveryAndItem(orderId);
        Delivery delivery = findOrder.getOrderItems().get(0).getDelivery();

        DeliveryDelegateDto dto = new DeliveryDelegateDto(delivery.getId(),
                member.getName(),
                findOrder.getOrderItems().get(0).getItem().getName(),
                delivery.getAddress());

        //rabbitMQ send
        try {
            deliverySender.sendOne(dto);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            orderService.cancelByRabbitMQError(findOrder);
            return new ResponseEntity<>("rabbitMQ send error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/orderList")
    public String orderList(Model model, @AuthenticationPrincipal Member member) {
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

        return "orderList";
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
