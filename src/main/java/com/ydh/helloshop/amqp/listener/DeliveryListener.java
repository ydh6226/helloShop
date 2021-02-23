package com.ydh.helloshop.amqp.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ydh.helloshop.amqp.dto.DeliveryFeedbackDto;
import com.ydh.helloshop.amqp.dto.DeliveryMessageType;
import com.ydh.helloshop.domain.delivery.Delivery;
import com.ydh.helloshop.exception.noSuchThat.NoSuchDelivery;
import com.ydh.helloshop.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DeliveryListener {

    private final DeliveryRepository deliveryRepository;

    private final String queueName = "delivery2shop.queue";

    @RabbitListener(queues = {queueName})
    @Transactional
    public void processMessage(String deliveryJsonString) {
        try {
            DeliveryFeedbackDto dto = new ObjectMapper().readValue(deliveryJsonString, DeliveryFeedbackDto.class);

            Delivery delivery = deliveryRepository.findById(dto.getDeliveryId())
                    .orElseThrow(() -> new NoSuchDelivery("can't find that delivery."));

            if (dto.getMessageType() == DeliveryMessageType.SHIPPED) {
                delivery.startDelivery();
                return;
            }
            delivery.endDelivery();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
