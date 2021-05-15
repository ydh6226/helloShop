package com.ydh.helloshop.application.domain.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ydh.helloshop.application.domain.Address;
import com.ydh.helloshop.application.domain.cart.Cart;
import com.ydh.helloshop.application.domain.order.Order;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.EnumType.STRING;


/**
 * primary key = member_id
 * unique key = email
 */
@Entity
@Getter
public class Member {

    @Id
    @GeneratedValue(generator = "member_id")
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(STRING)
    private MemberStatus status;

    @Embedded
    private Address address;

    private LocalDateTime joinDate;

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "member", cascade = ALL)
    private Cart cart;

    //연관관계 메서드
    protected void initCart() {
        this.cart = new Cart(this);
    }

    //setter
    public void createInfo(String name, String email, String password, Address address, MemberStatus memberStatus) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.status = memberStatus;
        joinDate = LocalDateTime.now();
        initCart();
    }
}
