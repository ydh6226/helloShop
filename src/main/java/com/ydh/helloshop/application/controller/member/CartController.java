package com.ydh.helloshop.application.controller.member;

import com.ydh.helloshop.application.controller.order.dto.CreateOrderParam;
import com.ydh.helloshop.application.controller.order.dto.RequestOrderInfo;
import com.ydh.helloshop.application.domain.cart.Cart;
import com.ydh.helloshop.application.domain.cart.CartItem;
import com.ydh.helloshop.application.domain.member.CurrentMember;
import com.ydh.helloshop.application.domain.member.Member;
import com.ydh.helloshop.application.exception.NotEnoughStockException;
import com.ydh.helloshop.application.repository.cart.CartRepository;
import com.ydh.helloshop.application.service.CartItemService;
import com.ydh.helloshop.application.service.CartService;
import com.ydh.helloshop.application.service.ItemService;
import com.ydh.helloshop.application.service.OrderService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final CartItemService cartItemService;
    private final ItemService itemService;
    private final OrderService orderService;

    private final CartRepository cartRepository;

    @GetMapping("/cart")
    public String cartView(Model model, @CurrentMember Member member) {
        model.addAttribute("cart", cartService.findOneByMemberId(member.getId()));
        return "cart/view";
    }

    @PostMapping("/cart/checkout")
    @ResponseBody
    public ResponseEntity<Object> checkout(@RequestBody CartCheckoutParam cartCheckoutParam, @CurrentMember Member member) {
        Cart cart = cartRepository.findOneByMemberId(member.getId());
        CreateOrderParam createOrderParam = new CreateOrderParam();

        List<Long> cartItemIds = cartCheckoutParam.getItemIds();
        List<CartItem> cartItems = cart.getCartItems();

        cartItems.stream()
                .filter(ci -> cartItemIds.contains(ci.getItem().getId()))
                .forEach(ci -> createOrderParam
                        .getRequestOrderInfos()
                        .add(new RequestOrderInfo(ci.getCount(), ci.getItem().getId())));

        try {
            return ResponseEntity.ok(orderService
                    .createOrder(createOrderParam, member.getId())
                    .getId());
        } catch (NotEnoughStockException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/cart/deleteCartItem")
    @ResponseBody
    public ResponseEntity<Void> deleteCartItem(@RequestBody OrderInfo orderInfo, @CurrentMember Member member) {
        cartService.deleteCartItemsByOrderId(orderInfo.getOrderId(), member.getId());
        return ResponseEntity.ok().build();
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
        private Long itemId;
        private int count;
    }

    @Data
    private static class CartCheckoutParam {
        private List<Long> itemIds = new ArrayList<>();
    }

    @Data
    private static class OrderInfo {
        private Long orderId;
    }
}
