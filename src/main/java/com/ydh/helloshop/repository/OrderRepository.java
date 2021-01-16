package com.ydh.helloshop.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ydh.helloshop.domain.*;
import com.ydh.helloshop.item.QItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static com.ydh.helloshop.domain.QOrder.order;
import static com.ydh.helloshop.domain.QOrderItem.orderItem;
import static com.ydh.helloshop.item.QItem.item;

@Repository
public class OrderRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public OrderRepository(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    public Long save(Order order) {
        em.persist(order);
        return order.getId();
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    public List<Order> findAll(OrderSearch orderSearch) {
        return query.select(order)
                .distinct()
                .from(order)
                .join(order.orderItems, orderItem)
                .join(orderItem.item, item)
                .where(memberEq(orderSearch.getMemberId()),
                        statusEq(orderSearch.getOrderStatus()),
                        itemNameLike(orderSearch.getItemName()))
                .limit(1000)
                .fetch();
    }

    private BooleanExpression memberEq(Long memberId) {
        if(memberId == null){
            return null;
        }
        return order.member.id.eq(memberId);
    }

    private BooleanExpression itemNameLike(String itemName) {
        if (!StringUtils.hasText(itemName)) {
            return null;
        }
        return orderItem.item.name.contains(itemName);
    }

    private BooleanExpression statusEq(OrderStatus statusCond) {
        if (statusCond == null) {
            return null;
        }
        return order.status.eq(statusCond);
    }
}