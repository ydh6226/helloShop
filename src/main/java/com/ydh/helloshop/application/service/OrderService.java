package com.ydh.helloshop.application.service;

import com.ydh.helloshop.application.controller.order.dto.CreateOrderParam;
import com.ydh.helloshop.application.domain.delivery.Delivery;
import com.ydh.helloshop.application.domain.item.Item;
import com.ydh.helloshop.application.domain.member.Member;
import com.ydh.helloshop.application.domain.order.Order;
import com.ydh.helloshop.application.domain.order.OrderItem;
import com.ydh.helloshop.application.exception.ItemException;
import com.ydh.helloshop.application.exception.noSuchThat.NoSuchMember;
import com.ydh.helloshop.application.repository.MemberRepository;
import com.ydh.helloshop.application.repository.item.ItemRepository;
import com.ydh.helloshop.application.repository.order.OrderItemRepository;
import com.ydh.helloshop.application.repository.order.OrderRepository;
import com.ydh.helloshop.application.repository.order.OrderSearch;
import com.ydh.helloshop.application.repository.order.dto.OrderParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private final OrderItemRepository orderItemRepository;

    @Transactional
    public Order createOrder(CreateOrderParam createOrderParam, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        Order order = new Order(member);

        createOrderParam.getRequestOrderInfos()
                .forEach(requestOrderInfo -> {
                    Item item = itemRepository.findById(requestOrderInfo.getItemId())
                            .orElseThrow(ItemException::noSuchItemException);

                    order.addOrderItem(createOrderItem(item, item.getPrice(), requestOrderInfo.getCount(),
                            new Delivery(member.getAddress())));
                });
        return orderRepository.save(order);
    }

    //단일주문
    @Transactional
    public Long orderOne(Long memberId, Long itemId, int count) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchMember("The member could not be found."));

        Item item = itemRepository.findById(itemId).orElseThrow(ItemException::noSuchItemException);

        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(createOrderItem(item, item.getPrice(), count, new Delivery(member.getAddress())));

        Order order = Order.createOrder(member, orderItems);
        return orderRepository.save(order).getId();
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

        return orderRepository.save(Order.createOrder(member, orderItems)).getId();
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

    /** 검색 **/
    public PageImpl<Order> findPagedOrdersBySearch(OrderSearch orderSearch, Pageable pageable) {
        return orderRepository.findPagedOrdersBySearch(orderSearch, pageable);
    }

    public Page<OrderParam> findOrderItemsBySearch(OrderSearch orderSearch, Pageable pageable) {
        return orderItemRepository.findBySearch(orderSearch, pageable);
    }

    public Order findOneWithDeliveryAndItem(Long id) {
        return orderRepository.findOneWithDeliveryAndItem(id);
    }

}
