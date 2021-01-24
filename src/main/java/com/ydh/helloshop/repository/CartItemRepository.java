package com.ydh.helloshop.repository;

import com.ydh.helloshop.domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    CartItem findByItemId(Long itemId);
}
