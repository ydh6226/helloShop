package com.ydh.helloshop.application.repository.order;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ydh.helloshop.application.domain.item.QItem;
import com.ydh.helloshop.application.domain.member.QMember;
import com.ydh.helloshop.application.domain.order.OrderItem;
import com.ydh.helloshop.application.domain.order.OrderStatus;
import com.ydh.helloshop.application.domain.order.QOrder;
import com.ydh.helloshop.application.domain.order.QOrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.querydsl.core.types.Order.ASC;
import static com.querydsl.core.types.Order.DESC;
import static com.ydh.helloshop.application.domain.item.QItem.item;
import static com.ydh.helloshop.application.domain.member.QMember.member;
import static com.ydh.helloshop.application.domain.order.QOrder.order;
import static com.ydh.helloshop.application.domain.order.QOrderItem.orderItem;

@RequiredArgsConstructor
public class OrderItemRepositoryImpl implements CustomOrderItemRepository {

    private final JPAQueryFactory query;

    @Override
    public PageImpl<OrderItem> findBySearch(OrderSearch orderSearch, Pageable pageable) {
        JPAQuery<OrderItem> tempResult = query.selectFrom(orderItem)
                .join(orderItem.order, order).fetchJoin()
                .join(orderItem.item, item).fetchJoin()
                .join(order.member, member)
                .where(member.id.eq(orderSearch.getMemberId()))
                .where(itemNameLike(orderSearch.getItemName()))
                .where(statusEq(orderSearch.getOrderStatus()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        for (Sort.Order o : pageable.getSort()) {
            Order order = o.isAscending()? ASC : DESC;

            PathBuilder<Object> pathBuilder = new PathBuilder<>(Object.class, "order");
            tempResult.orderBy(new OrderSpecifier(order, pathBuilder.get(o.getProperty())));
        }

        QueryResults<OrderItem> results = tempResult.fetchResults();

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
