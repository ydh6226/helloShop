package com.ydh.helloshop.service;

import com.ydh.helloshop.domain.Cart;
import com.ydh.helloshop.domain.CartItem;
import com.ydh.helloshop.item.Item;
import com.ydh.helloshop.repository.CartItemRepository;
import com.ydh.helloshop.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

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
    @Transactional(readOnly = false)
    public Long addToCart(Long memberId, Item item, int count) {
        Cart cart = cartRepository.findOneByMemberId(memberId);

        //장바구니에 해당 상품이 이미 존재 하는경우
        List<CartItem> cartItems = cartItemRepository.findByItemId(item.getId());
        if (!cartItems.isEmpty()){
            CartItem cartItem = cartItems.get(0);
            cartItem.changeItemCount(count);
            return cart.getId();
        }

        CartItem cartItem = CartItem.createCartItem(item, count);
        cart.addItem(cartItem);
        return cart.getId();
    }

    //장바구니에서 삭제
    @Transactional(readOnly = false)
    public Long deleteItemsFrommCart(Long memberId, List<Long> cartItemIds) {
        Cart cart = cartRepository.findOneByMemberId(memberId);
        List<CartItem> cartItems = cartItemRepository.findAllWithItemByIdIn(cartItemIds);
        cartItems.forEach(cart::deleteItem);

        cartItemRepository.deleteByIdInQuery(cartItemIds);
        return cart.getId();
    }

    //장바구니 주문시 주문된 상품 장바구니에서 삭제
    @Transactional(readOnly = false)
    public void checkout(Long cartId, List<Long> itemIds) {
        Cart cart = cartRepository.findOneById(cartId);

        List<CartItem> cartItems = cartItemRepository.findAllByItemIdIn(itemIds);
        cartItems.forEach(cart::deleteItem);

        cartItemRepository.deleteByItemIdsInQuery(itemIds);
    }
}
