package com.ydh.helloshop.domain;

import com.ydh.helloshop.domain.delivery.Delivery;
import com.ydh.helloshop.domain.delivery.DeliveryStatus;
import com.ydh.helloshop.domain.order.Order;
import com.ydh.helloshop.item.Item;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

/**
 * createOrderItem() 안쓰고 생성자를 사용할 수 있기 때문에
 * 접근 지정자가 protected 인 기본 생성자 필요. (jpa 스펙으로 기본 생성자 필요)
 * 따라서 createOrderItem()를 사용해야 OrderItem 생성 가능
 */

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class OrderItem {

    @Id
    @GeneratedValue(generator = "order_item_id")
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "Item_id")
    private Item item;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;

    private int count;

    @OneToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    //생성자
    protected OrderItem(Item item, int orderPrice, int count) {
        this.item = item;
        this.orderPrice = orderPrice;
        this.count = count;
    }

    //생성 메서드
    public static OrderItem createOrderItem(Item item, int orderPrice, int count, Delivery delivery) {
        OrderItem orderItem = new OrderItem(item, orderPrice, count);
        orderItem.item.removeStock(count);
        orderItem.changeDelivery(delivery);
        return orderItem;
    }

    //setter
    public void initOrder(Order order) {
        this.order = order;
    }

    //연관 관계 메서드
    private void changeDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.changeOrderItem(this);
    }

    //== 비즈니스 로직 ==//
    //주문 취소
    public void cancel() {
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }

        item.addStock(count);
    }

    //== 조회 로직 ==//
    public int getTotalPrice() {
        return orderPrice * count;
    }

}
