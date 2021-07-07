package com.ydh.helloshop.application.repository.order;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ydh.helloshop.application.domain.delivery.QDelivery;
import com.ydh.helloshop.application.domain.item.Item;
import com.ydh.helloshop.application.domain.item.QItem;
import com.ydh.helloshop.application.domain.order.Order;
import com.ydh.helloshop.application.domain.order.OrderStatus;
import com.ydh.helloshop.application.domain.order.QOrder;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Objects;

import static com.ydh.helloshop.application.domain.delivery.QDelivery.delivery;
import static com.ydh.helloshop.application.domain.item.QItem.item;
import static com.ydh.helloshop.application.domain.member.QMember.member;
import static com.ydh.helloshop.application.domain.order.QOrder.order;
import static com.ydh.helloshop.application.domain.order.QOrderItem.orderItem;

@Repository
public class OrderRepository extends QuerydslRepositorySupport {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public OrderRepository(EntityManager em) {
        super(Order.class);
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
                .join(orderItem.item, item).fetchJoin()
                .where(order.id.eq(id))
                .fetchOne();
    }

    public Order findOrderForPublish(Long id) {
        return query.selectFrom(order)
                .join(order.member, member).fetchJoin()
                .join(order.orderItems, orderItem).fetchJoin()
                .join(orderItem.delivery, delivery).fetchJoin()
                .join(orderItem.item, item).fetchJoin()
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

    public void deleteOne(Order order) {
        em.remove(order);
    }

    public PageImpl<Order> findPagedOrdersBySearch(OrderSearch orderSearch, Pageable pageable) {
        QueryResults<Order> queryResults = query.selectFrom(order)
                .join(order.orderItems, orderItem)
                .join(orderItem.item, item)
                .join(order.member, member)
                .where(memberEq(orderSearch.getMemberId()),
                        itemNameLike(orderSearch.getItemName()),
                        statusEq(orderSearch.getOrderStatus()),
                        order.status.ne(OrderStatus.CREATED))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(order.orderDate.desc())
                .distinct()
                .fetchResults();

        return new PageImpl<>(queryResults.getResults(), pageable, queryResults.getTotal());
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

    private BooleanExpression statusEq(OrderStatus status) {
        if (status == null) {
            return null;
        }
        return order.status.eq(status);
    }
}
