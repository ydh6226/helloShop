package com.ydh.helloshop.controller.member;

import com.ydh.helloshop.domain.Member;
import com.ydh.helloshop.service.CartItemService;
import com.ydh.helloshop.service.CartService;
import com.ydh.helloshop.service.OrderService;
import com.ydh.helloshop.service.item.ItemServiceImpl;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final CartItemService cartItemService;
    private final ItemServiceImpl itemService;
    private final OrderService orderService;

    @GetMapping("/cart")
    public String cartView(Model model, @AuthenticationPrincipal Member member) {
        if (member != null) {
            model.addAttribute("cart", cartService.findOneByMemberId(member.getId()));
        }
        else {
            model.addAttribute("cart", cartService.findOneByMemberId(2L));
        }
        return "/cartView";
    }

    @PostMapping("/cart/checkout")
    @ResponseBody
    public void cartCheckout(@RequestBody OrderInfoDto orderInfoDto, @AuthenticationPrincipal Member member) {
        orderService.orderMultiple(member.getId(), orderInfoDto.getItemIds(), orderInfoDto.getCounts());
        cartService.checkout(orderInfoDto.getCartId(), orderInfoDto.getItemIds());

        //amqp send()
    }

    @ResponseBody
    @PostMapping("/cart/add")
    public void addToCart(@RequestBody SimpleItemDto itemDto, @AuthenticationPrincipal Member member) {
        cartService.addToCart(member.getId(), itemService.findOne(itemDto.getItemId()), itemDto.count);
    }

    @ResponseBody
    @PostMapping("/cart/delete")
    public String deleteFromCart(@RequestBody HashMap<String, List<Long>> mapDto, @AuthenticationPrincipal Member member) {
        List<Long> ids = mapDto.get("ids");

        cartService.deleteItemsFrommCart(member.getId(), ids);
        return "/cart";
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
