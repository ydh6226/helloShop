package com.ydh.helloshop.application.service;

import com.ydh.helloshop.application.domain.cart.Cart;
import com.ydh.helloshop.application.domain.cart.CartItem;
import com.ydh.helloshop.application.domain.item.Item;
import com.ydh.helloshop.application.domain.member.Member;
import com.ydh.helloshop.application.domain.order.Order;
import com.ydh.helloshop.application.domain.order.OrderItem;
import com.ydh.helloshop.application.repository.CartItemRepository;
import com.ydh.helloshop.application.repository.CartRepository;
import com.ydh.helloshop.application.repository.MemberRepository;
import com.ydh.helloshop.application.repository.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;

    //장바구니 조회
    public Cart findOneByMemberId(Long memberId) {
        return cartRepository.findOneByMemberIdWithItem(memberId);
    }

    public Cart findOne(Long cartId) {
        return cartRepository.findOneById(cartId);
    }

    public int changeCartItemCount(Long cartId, CartItem cartItem, int count) {
        Cart cart = cartRepository.findOneById(cartId);
        return cart.changeItemCount(cartItem, count);
    }

    //장바구니에 추가
    @Transactional
    public Long addToCart(Long memberId, Item item, int count) {
        Cart cart = cartRepository.findOneByMemberId(memberId);
        Optional<CartItem> findCartItem = cartItemRepository.findOneByCartIdAndItemId(cart.getId(), item.getId());

        //장바구니에 해당 상품이 없는 경우 장바구니에 상품 추가
        if (findCartItem.isEmpty()) {
            CartItem cartItem = CartItem.createCartItem(item, count);
            cart.addItem(cartItem);
            return cart.getId();
        }

        //장바구니에 해당 상품이 있는 경우 입력받은 개수로 상품 개수 변경
        findCartItem.get().changeItemCount(count);
        return cart.getId();
    }

    //장바구니에서 삭제
    @Transactional
    public Long deleteItemsFrommCart(Long memberId, List<Long> cartItemIds) {
        Cart cart = cartRepository.findOneByMemberId(memberId);
        List<CartItem> cartItems = cartItemRepository.findAllWithItemByIdIn(cartItemIds);
        cartItems.forEach(cart::deleteItem);

        cartItemRepository.deleteByIdInQuery(cartItemIds);
        return cart.getId();
    }

    //장바구니 주문시 주문된 상품 장바구니에서 삭제
    @Transactional
    public void checkout(Long cartId, List<Long> itemIds) {
        Cart cart = cartRepository.findOneById(cartId);

        List<CartItem> cartItems = cartItemRepository.findAllByItemIdIn(itemIds);
        cartItems.forEach(cart::deleteItem);

        cartItemRepository.deleteByItemIdsInQuery(itemIds);
    }

    @Transactional
    public void deleteCartItemsByOrderId(Long orderId, Long memberId) {
        Order order = orderRepository.findOne(orderId);
        if (!order.getMember().getId().equals(memberId)) {
            throw new AccessDeniedException("접근 권한이 없습니다.");
        }

        List<Long> itemIds = new ArrayList<>();
        for (OrderItem orderItem : order.getOrderItems()) {
            itemIds.add(orderItem.getItem().getId());
        }

        cartItemRepository.deleteCartItemsByItemIdsAndMemberId(itemIds, memberId);
    }
}
