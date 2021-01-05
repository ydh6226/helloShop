package com.ydh.helloshop.service;

import com.ydh.helloshop.domain.Delivery;
import com.ydh.helloshop.domain.Member;
import com.ydh.helloshop.domain.Order;
import com.ydh.helloshop.domain.OrderItem;
import com.ydh.helloshop.item.Item;
import com.ydh.helloshop.repository.ItemRepository;
import com.ydh.helloshop.repository.MemberRepository;
import com.ydh.helloshop.repository.OrderRepository;
import com.ydh.helloshop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.ydh.helloshop.domain.Order.createOrder;
import static com.ydh.helloshop.domain.OrderItem.*;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    //단일주문
    @Transactional
    public Long orderOne(Long memberId, Long itemId, int count) {
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);
        Delivery delivery = new Delivery(member.getAddress());

        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(createOrderItem(item, item.getPrice(), count));

        Order order = createOrder(member, delivery, orderItems);
        return orderRepository.save(order);
    }

    //복수주문
    @Transactional
    public Long orderMultiple(Long memberId, List<Long> itemIds, List<Integer> counts) {
        Member member = memberRepository.findOne(memberId);
        List<OrderItem> orderItems = new ArrayList<>();

        int size = itemIds.size();
        for (int index = 0; index < size; index++) {
            Item item = itemRepository.findOne(itemIds.get(index));
            orderItems.add(createOrderItem(item, item.getPrice(), counts.get(index)));
        }

        Delivery delivery = new Delivery(member.getAddress());

        Order order = createOrder(member, delivery, orderItems);
        return orderRepository.save(order);
    }

    //주문 취소
    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findOne(orderId);
        order.cancel();
    }

    //검색
    public List<Order> findAll(OrderSearch orderSearch) {
        return orderRepository.findAll(orderSearch);
    }
}
