package com.ydh.helloshop.application.repository;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.ydh.helloshop.application.domain.cart.QCart.cart;
import static com.ydh.helloshop.application.domain.cart.QCartItem.cartItem;

public class CartItemRepositoryImpl implements CustomCartItemRepository {

    private final JPAQueryFactory query;
    private final EntityManager em;

    public CartItemRepositoryImpl(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public void deleteCartItemsByItemIdsAndMemberId(List<Long> itemIds, Long memberId) {
        query.delete(cartItem)
                .where(cartItem.item.id.in(itemIds))
                .where(cartItem.cart.id.in(
                        JPAExpressions.select(cart.id)
                                .from(cart)
                                .where(cart.member.id.eq(memberId))))
                .execute();

        em.flush();
        em.clear();
    }
}
