package com.ydh.helloshop.application.service;

import com.ydh.helloshop.application.domain.delivery.Delivery;
import com.ydh.helloshop.application.domain.item.Item;
import com.ydh.helloshop.application.domain.member.Member;
import com.ydh.helloshop.application.domain.order.Order;
import com.ydh.helloshop.application.domain.order.OrderItem;
import com.ydh.helloshop.application.exception.noSuchThat.NoSuchItem;
import com.ydh.helloshop.application.exception.noSuchThat.NoSuchMember;
import com.ydh.helloshop.application.repository.MemberRepository;
import com.ydh.helloshop.application.repository.item.ItemRepository;
import com.ydh.helloshop.application.repository.order.OrderRepository;
import com.ydh.helloshop.application.repository.order.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.ydh.helloshop.application.domain.order.Order.createOrder;
import static com.ydh.helloshop.application.domain.order.OrderItem.createOrderItem;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
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
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchMember("The member could not be found."));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NoSuchItem("The Item could not be found."));

        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(createOrderItem(item, item.getPrice(), count, new Delivery(member.getAddress())));

        Order order = createOrder(member, orderItems);
        return orderRepository.save(order);
    }

    //복수주문
    @Transactional
    public Long orderMultiple(Long memberId, List<Long> itemIds, List<Integer> counts) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchMember("The member could not be found."));

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
                .map(o -> createOrderItem(o, o.getPrice(), itemCountMap.get(o.getId()),
                        new Delivery(member.getAddress())))
                .collect(toList());

        return orderRepository.save(createOrder(member, orderItems));
    }

    //주문 취소
    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findOne(orderId);
        order.cancel();
    }

    //rabbitMQ send 과정중 에러 발생했을 때 주문 취소
    @Transactional
    public void cancelByRabbitMQError(Order order) {
        order.getMember().getOrders().remove(order);
        orderRepository.deleteOne(order);
    }

    //검색
    public List<Order> findAll(OrderSearch orderSearch) {
        return orderRepository.findAll(orderSearch);
    }

    public Order findOneWithDeliveryAndItem(Long id) {
        return orderRepository.findOneWithDeliveryAndItem(id);
    }
}
