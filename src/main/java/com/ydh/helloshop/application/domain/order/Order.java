package com.ydh.helloshop.application.domain.order;

import com.ydh.helloshop.application.domain.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;

/**
 * primary_key = order_id
 */

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id
    @GeneratedValue(generator = "order_id")
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = LAZY, cascade = PERSIST)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    private LocalDateTime orderDate;

    @Enumerated(STRING)
    private OrderStatus status;

    //생성 메서드
    public Order(Member member) {
        this.orderDate = LocalDateTime.now();
        this.status = OrderStatus.CREATED;
        this.member = member;
    }

    public void setStatusPayed() {
        status = OrderStatus.PAID;
    }

    private void setStatusCancel() {
        status = OrderStatus.CANCELED;
    }


    //== 연관 관계 메서드 ==//
    private void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.initOrder(this);
    }

    //주문 생성 메서드
    public static Order createOrder(Member member, List<OrderItem> orderItems) {
        Order order = new Order(member);
        order.setMember(member);
        orderItems.forEach(order::addOrderItem);
        return order;
    }

    //== 비즈니스 로직 ==//
    //주문 취소
    public void cancel() {
        try {
            orderItems.forEach(OrderItem::cancel);
            setStatusCancel();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    //== 조회 로직 ==//
    //전체 주문 가격 조회
    public int getTotalPrice() {
        return orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum();
    }
}
