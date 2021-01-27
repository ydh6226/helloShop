package com.ydh.helloshop.service;

import com.ydh.helloshop.domain.CartItem;
import com.ydh.helloshop.exception.NoSuchCartItem;
import com.ydh.helloshop.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CartItemService {

    private final CartItemRepository cartItemRepository;

    @Transactional
    public int changeCartItemCount(Long cartItemId, int count) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new NoSuchCartItem("The category could not be found."));

        cartItem.changeItemCount(count);
        return cartItem.getTotalPrice();
    }

    @Transactional
    public void deleteByItemIds(List<Long> itemIds) {
        cartItemRepository.deleteByItemIdsInQuery(itemIds);
    }

}
