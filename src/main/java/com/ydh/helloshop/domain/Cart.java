package com.ydh.helloshop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Cart {

    @Id @GeneratedValue(generator = "cart_id")
    @Column(name = "cart_id")
    private Long id;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "cart", cascade = ALL)
    private List<CartItem> cartItems = new ArrayList<>();

    private int totalPrice;

    //생성자
    public Cart(Member member) {
        this.member = member;
        this.totalPrice = 0;
    }

    //연관관계 메소드
    protected void changeItem(CartItem cartItem) {
        cartItems.add(cartItem);
        cartItem.changeCart(this);
    }

    //== 비즈니스로직 ==//
    public void addItem(CartItem cartItem) {
        changeItem(cartItem);
        int price = cartItem.getItem().getPrice();
        int count = cartItem.getCount();

        totalPrice += price * count;
    }

    public void deleteItem(CartItem cartItem) {
        cartItems.remove(cartItem);

        int price = cartItem.getItem().getPrice();
        int count = cartItem.getCount();

        totalPrice -= price * count;
    }

    public int changeItemCount(CartItem cartItem, int count) {
        CartItem findCartItem = cartItems.get(cartItems.indexOf(cartItem));
        totalPrice -= findCartItem.getTotalPrice();
        findCartItem.changeItemCount(count);
        totalPrice += findCartItem.getTotalPrice();

        return totalPrice;
    }

    //CartItem 수량 변경으로 인해 CartItem의 totalPrice가 변경될 때 Cart의 totalPrice도 반영한다
    public int reComputeTotalPrice(int preItemTotalPrice, int newItemTotalPrice){
        totalPrice = totalPrice - preItemTotalPrice + newItemTotalPrice;
        return totalPrice;
    }
}
