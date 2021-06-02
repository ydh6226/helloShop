package com.ydh.helloshop.application.controller.member;

import com.ydh.helloshop.application.domain.member.CurrentMember;
import com.ydh.helloshop.application.domain.member.Member;
import com.ydh.helloshop.application.domain.order.Order;
import com.ydh.helloshop.application.domain.order.OrderItem;
import com.ydh.helloshop.application.service.CartItemService;
import com.ydh.helloshop.application.service.CartService;
import com.ydh.helloshop.application.service.OrderService;
import com.ydh.helloshop.application.service.ItemService;
import com.ydh.helloshop.infra.amqp.dto.DeliveryPublishParam;
import com.ydh.helloshop.infra.amqp.sender.DeliveryPublisher;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final CartItemService cartItemService;
    private final ItemService itemService;
    private final OrderService orderService;
    private final DeliveryPublisher deliveryPublisher;

    @GetMapping("/cart")
    public String cartView(Model model, @CurrentMember Member member) {
        model.addAttribute("cart", cartService.findOneByMemberId(member.getId()));
        return "cart/view";
    }

    @PostMapping("/cart/checkout")
    @ResponseBody
    public ResponseEntity<String> cartCheckout(@RequestBody OrderInfoDto orderInfoDto, @CurrentMember Member member) {
        Long orderId = orderService.orderMultiple(member.getId(), orderInfoDto.getItemIds(), orderInfoDto.getCounts());

        Order findOrder = orderService.findOneWithDeliveryAndItem(orderId);
        List<OrderItem> orderItems = findOrder.getOrderItems();

        List<DeliveryPublishParam> dtos = orderItems.stream().map(oi ->
                new DeliveryPublishParam(oi.getDelivery().getId(), member.getName(),
                        oi.getItem().getName(), oi.getDelivery().getAddress()))
                .collect(Collectors.toList());

        //RabbitMQ send
        try {
            deliveryPublisher.send(dtos);
            cartService.checkout(orderInfoDto.getCartId(), orderInfoDto.getItemIds());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            // publish 실패하면 생성했던 주문 삭제
            orderService.cancelByRabbitMQError(findOrder);
            return new ResponseEntity<>("rabbitMQ send error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    @PostMapping("/cart/add")
    public void addToCart(@RequestBody SimpleItemDto itemDto, @CurrentMember Member member) {
        cartService.addToCart(member.getId(), itemService.findOne(itemDto.getItemId()), itemDto.count);
    }

    @ResponseBody
    @PostMapping("/cart/delete")
    public String deleteFromCart(@RequestBody HashMap<String, List<Long>> mapDto, @CurrentMember Member member) {
        List<Long> ids = mapDto.get("ids");

        cartService.deleteItemsFrommCart(member.getId(), ids);
        return "cart";
    }

    @ResponseBody
    @PostMapping("/cart/change")
    public SimpleCartItemDto changeCartItemCount(@RequestBody SimpleCartItemDto dto) {
        List<Integer> result = cartItemService.changeCartItemCount(dto.getCartItemId(), dto.getCount());

        dto.setTotalPrice(result.get(0));
        dto.setOrderTotalPrice(result.get(1));
        return dto;
    }

    @Data
    private static class SimpleCartItemDto {
        Long cartItemId;
        int count;
        int totalPrice;
        int orderTotalPrice;
    }

    @Data
    private static class SimpleItemDto {
        Long itemId;
        int count;
    }

    @Data
    static class OrderInfoDto {
        List<Integer> counts;
        List<Long> itemIds;
        Long cartId;
    }
}
