package com.ydh.helloshop.service;

import com.ydh.helloshop.domain.Delivery;
import com.ydh.helloshop.domain.Member;
import com.ydh.helloshop.domain.Order;
import com.ydh.helloshop.domain.OrderItem;
import com.ydh.helloshop.exception.NoSuchItem;
import com.ydh.helloshop.item.Item;
import com.ydh.helloshop.repository.item.ItemRepository;
import com.ydh.helloshop.repository.MemberRepository;
import com.ydh.helloshop.repository.OrderRepository;
import com.ydh.helloshop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.ydh.helloshop.domain.Order.createOrder;
import static com.ydh.helloshop.domain.OrderItem.*;
import static java.util.stream.Collectors.*;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

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
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NoSuchItem("Register the Item!!"));
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

        //iterator 사용
/*
        Iterator<Long> idItr = itemIds.iterator();
        Iterator<Integer> countItr = counts.iterator();

        Map<Long, Integer> itemCountMap = range(0, itemIds.size()).boxed()
                .collect(toMap(i -> idItr.next(), i -> countItr.next()));
*/

        //get 사용
        Map<Long, Integer> itemCountMap = range(0, itemIds.size()).boxed()
                .collect(toMap(itemIds::get, counts::get));

        List<OrderItem> orderItems = itemRepository.findMultiple(itemIds).stream()
                .map(o -> createOrderItem(o, o.getPrice(), itemCountMap.get(o.getId())))
                .collect(toList());

        Delivery delivery = new Delivery(member.getAddress());

        return orderRepository.save(createOrder(member, delivery, orderItems));
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
