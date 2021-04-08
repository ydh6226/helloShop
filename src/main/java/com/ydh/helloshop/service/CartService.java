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
import java.util.Optional;

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
}
