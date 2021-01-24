package com.ydh.helloshop.service;

import com.ydh.helloshop.domain.Cart;
import com.ydh.helloshop.domain.CartItem;
import com.ydh.helloshop.item.Item;
import com.ydh.helloshop.repository.CartRepository;
import com.ydh.helloshop.repository.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    //장바구니 조회
    public Cart findOneByMemberId(Long memberId) {
        return cartRepository.findOneByMemberId(memberId);
    }

    public Cart findOne(Long cartId) {
        return cartRepository.findOneById(cartId);
    }

    //장바구니에 추가
    @Transactional(readOnly = false)
    public Long addToCart(Long memberId, Item item, int count) {
        Cart cart = cartRepository.findOneByMemberId(memberId);
        CartItem cartItem = CartItem.createCartItem(item, count);
        cart.addItem(cartItem);
        return cart.getId();
    }

    //장바구니에서 삭제
    @Transactional(readOnly = false)
    public Long deleteFromCart(Long memberId, CartItem cartItem) {
        Cart cart = cartRepository.findOneByMemberId(memberId);
        cart.deleteItem(cartItem);
        return cart.getId();
    }


}
