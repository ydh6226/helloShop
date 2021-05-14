package com.ydh.helloshop.application.repository;

import com.ydh.helloshop.application.domain.Cart;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @EntityGraph(attributePaths = {"cartItems"})
    Cart findOneByMemberId(Long memberId);

    @Query("select c from Cart c left outer join fetch c.cartItems ci left outer join fetch ci.item where c.member.id = :memberId ")
    Cart findOneByMemberIdWithItem(@Param("memberId") Long memberId);

    @EntityGraph(attributePaths = {"cartItems"})
    Cart findOneById(Long cartId);

    @Query("select c from Cart c left outer join fetch c.cartItems ci left outer join fetch ci.item where c.member.id = :memberId ")
    List<Cart> findAllByMemberIdWithItem(@Param("memberId") Long memberId);
}
