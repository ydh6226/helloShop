package com.ydh.helloshop.application.service;

import com.ydh.helloshop.application.domain.Address;
import com.ydh.helloshop.application.domain.member.CreateMemberParam;
import com.ydh.helloshop.application.domain.member.Member;
import com.ydh.helloshop.application.domain.order.Order;
import com.ydh.helloshop.application.domain.order.OrderItem;
import com.ydh.helloshop.application.repository.MemberRepository;
import com.ydh.helloshop.application.repository.item.ItemRepository;
import com.ydh.helloshop.application.repository.order.OrderRepository;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static com.ydh.helloshop.application.domain.member.MemberStatus.CUSTOMER;

@Transactional
@SpringBootTest
class OrderServiceSpringTest {

    @Autowired
    OrderService orderService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    private void createCustomerMember() {
        Member member = Member.createMember(new CreateMemberParam("cus", "cus","cus", CUSTOMER,
                new Address("경기", "행신로", "143번길")));
        memberRepository.save(member);


        new Order(member);
    }


    @Test
    @DisplayName("주문 목록 조회")
    void findOrderItemsBySearch() {

    }

}