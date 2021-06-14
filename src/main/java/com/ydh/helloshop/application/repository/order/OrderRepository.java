package com.ydh.helloshop.application.repository.order;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ydh.helloshop.application.domain.order.Order;
import com.ydh.helloshop.application.domain.order.OrderStatus;
import com.ydh.helloshop.application.domain.order.QOrderItem;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static com.ydh.helloshop.application.domain.item.QItem.item;
import static com.ydh.helloshop.application.domain.member.QMember.member;
import static com.ydh.helloshop.application.domain.order.QOrder.order;
import static com.ydh.helloshop.application.domain.order.QOrderItem.orderItem;

@Repository
public class OrderRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public OrderRepository(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    public Order save(Order order) {
        em.persist(order);
        return order;
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    public Order findOrderWithOrderItemsAndMember(Long id) {
        return query.selectFrom(order)
                .join(order.member, member).fetchJoin()
                .join(order.orderItems, orderItem).fetchJoin()
                .where(order.id.eq(id))
                .fetchOne();
    }

    public Order findOneWithDeliveryAndItem(Long id) {
        return em.createQuery("select o from Order o" +
                " join fetch o.orderItems oi" +
                " join fetch oi.delivery" +
                " join fetch oi.item" +
                " where o.id = :id", Order.class)
                .setParameter("id", id)
                .getSingleResult();
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

    public void deleteOne(Order order) {
        em.remove(order);
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
