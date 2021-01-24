package com.ydh.helloshop.domain;

import com.ydh.helloshop.item.Item;
import lombok.AccessLevel;
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

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private int count;

    //생성자
    protected CartItem(Item item, int count) {
        this.item = item;
        this.count = count;
    }

    //setter
    public void changeCart(Cart cart) {
        this.cart = cart;
    }

    //생성 메소드
    public static CartItem createCartItem(Item item, int count) {
        return new CartItem(item, count);
    }
}
