package com.ydh.helloshop.repository;

import com.ydh.helloshop.domain.Cart;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @EntityGraph(attributePaths = {"cartItems"})
    Cart findOneByMemberId(Long memberId);

    @EntityGraph(attributePaths = {"cartItems"})
    Cart findOneById(Long cartId);
}
