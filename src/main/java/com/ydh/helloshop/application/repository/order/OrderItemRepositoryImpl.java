package com.ydh.helloshop.application.repository.order;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ydh.helloshop.application.domain.order.OrderStatus;
import com.ydh.helloshop.application.repository.order.dto.OrderParam;
import com.ydh.helloshop.application.repository.order.dto.QOrderParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import static com.querydsl.core.types.Order.ASC;
import static com.querydsl.core.types.Order.DESC;
import static com.ydh.helloshop.application.domain.delivery.QDelivery.delivery;
import static com.ydh.helloshop.application.domain.item.QItem.item;
import static com.ydh.helloshop.application.domain.member.QMember.member;
import static com.ydh.helloshop.application.domain.order.QOrder.order;
import static com.ydh.helloshop.application.domain.order.QOrderItem.orderItem;

@RequiredArgsConstructor
public class OrderItemRepositoryImpl implements OrderItemRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public Page<OrderParam> findBySearch(OrderSearch orderSearch, Pageable pageable) {
        QueryResults<OrderParam> results = query.select(new QOrderParam(item.id, item.name, order.orderDate, item.price, orderItem.count, delivery.status))
                .from(orderItem)
                .join(orderItem.order, order)
                .join(orderItem.item, item)
                .join(orderItem.delivery, delivery)
                .join(order.member, member)
                .where(order.status.ne(OrderStatus.CREATED))
                .where(member.id.eq(orderSearch.getMemberId()))
                .where(itemNameLike(orderSearch.getItemName()))
                .where(statusEq(orderSearch.getOrderStatus()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(order.orderDate.desc())
                .fetchResults();

        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    private BooleanExpression itemNameLike(String itemName) {
        if (!StringUtils.hasText(itemName)) {
            return null;
        }
        return item.name.contains(itemName);
    }

    private BooleanExpression statusEq(OrderStatus status) {
        if (status == null) {
            return null;
        }
        return order.status.eq(status);
    }
}
