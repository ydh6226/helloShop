package com.ydh.helloshop.application.domain.cart;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ydh.helloshop.application.exception.ExceedMaximumQuantity;
import com.ydh.helloshop.application.domain.item.Item;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class CartItem {

    @Id @GeneratedValue(generator = "cart_item_id")
    @Column(name = "cart_item_id")
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private int count;

    private int totalPrice;

    //생성자
    protected CartItem(Item item, int count) {
        this.item = item;
        this.count = count;
        totalPrice = item.getPrice() * count;
    }

    //setter
    public void changeCart(Cart cart) {
        this.cart = cart;
    }

    //생성 메소드
    public static CartItem createCartItem(Item item, int count) {
        return new CartItem(item, count);
    }

    //== 비즈니스 로직 ==//
    public int changeItemCount(int count) {
        if (count > 10) {
            throw new ExceedMaximumQuantity("The maximum number of items is 10.");
        }
        this.count = count;
        totalPrice = item.getPrice() * count;
        return totalPrice;
    }
}
