package com.ydh.helloshop.application.domain.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ydh.helloshop.application.domain.Address;
import com.ydh.helloshop.application.domain.cart.Cart;
import com.ydh.helloshop.application.domain.order.Order;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
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
    private void initCart() {
        this.cart = new Cart(this);
    }

    public static Member createMember(CreateMemberParam createMemberParam) {
        Member member = new Member();

        member.name = createMemberParam.getName();
        member.email = createMemberParam.getEmail();
        member.password = createMemberParam.getPassword();
        member.address = createMemberParam.getAddress();
        member.status = createMemberParam.getMemberStatus();
        member.joinDate = LocalDateTime.now();
        member.initCart();

        return member;
    }
}
