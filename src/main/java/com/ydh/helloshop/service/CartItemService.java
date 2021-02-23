package com.ydh.helloshop.service;

import com.ydh.helloshop.domain.CartItem;
import com.ydh.helloshop.exception.noSuchThat.NoSuchCartItem;
import com.ydh.helloshop.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CartItemService {

    private final CartItemRepository cartItemRepository;

    private static NoSuchCartItem exception() {
        return new NoSuchCartItem("The category could not be found.");
    }

    public CartItem findOne(Long cartItemId) {
        return cartItemRepository.findById(cartItemId)
                .orElseThrow(CartItemService::exception);
    }


    public List<CartItem> findAllByItemIdIn(List<Long> itemIds) {
        return cartItemRepository.findAllByItemIdIn(itemIds);
    }

    /**
     * @return List[0] = CartItem: totalPrice,
     * List[1] = Cart: totalPrice
     */
    @Transactional
    public List<Integer> changeCartItemCount(Long cartItemId, int count) {
        CartItem cartItem = cartItemRepository.findByIdWithCartAndItem(cartItemId);

        int preTotalPrice = cartItem.getTotalPrice();
        int newTotalPrice = cartItem.changeItemCount(count);

        int orderTotalPrice = cartItem.getCart().reComputeTotalPrice(preTotalPrice, newTotalPrice);

        ArrayList<Integer> result = new ArrayList<>();
        result.add(cartItem.getTotalPrice());
        result.add(orderTotalPrice);

        return result;
    }

    @Transactional
    public void deleteByItemIds(List<Long> itemIds) {
        cartItemRepository.deleteByItemIdsInQuery(itemIds);
    }

}
