package com.ydh.helloshop.domain;

import com.ydh.helloshop.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.*;
import static lombok.AccessLevel.*;

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
    @GeneratedValue(generator = "order_intem_id")
    @Column(name = "orderItem_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "Item_id")
    private Item item;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;

    private int count;

    //생성자
    protected OrderItem(Item item, int orderPrice, int count) {
        this.item = item;
        this.orderPrice = orderPrice;
        this.count = count;
    }

    //생성 메서드
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem(item, orderPrice, count);
        orderItem.item.removeStock(count);
        return orderItem;
    }

    //setter
    public void initOrder(Order order) {
        this.order = order;
    }

    //== 비즈니스 로직 ==//

    //주문 취소
    public void cancel() {
        item.addStock(count);
    }

    //== 조회 로직 ==//
    public int getTotalPrice() {
        return orderPrice * count;
    }

}
