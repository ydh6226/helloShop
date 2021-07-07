package com.ydh.helloshop.application.service;

import com.ydh.helloshop.application.domain.Address;
import com.ydh.helloshop.application.domain.member.Member;
import com.ydh.helloshop.application.domain.order.Order;
import com.ydh.helloshop.application.repository.order.OrderRepository;
import com.ydh.helloshop.infra.amqp.dto.DeliveryPublishParam;
import com.ydh.helloshop.infra.amqp.sender.DeliveryPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DeliveryService {

    private final OrderRepository orderRepository;

    private final DeliveryPublisher deliveryPublisher;

    public void publishDeliveryInfo(Long orderId) {
        Order order = orderRepository.findOne(orderId);

        Member member = order.getMember();
        String memberName = member.getName();
        Address address = member.getAddress();

        List<DeliveryPublishParam> deliveryParams = order.getOrderItems().stream()
                .map(oi -> new DeliveryPublishParam(oi.getDelivery().getId(),
                        memberName,
                        oi.getItem().getName(),
                        address))
                .collect(Collectors.toList());

        try {
            deliveryPublisher.send(deliveryParams);
        } catch (IllegalStateException e) {
            log.error("주문번호 = {} : 배송정보 전송을 실패했습니다.", orderId);
        }
    }


}
