package com.ydh.helloshop.infra.amqp.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ydh.helloshop.infra.amqp.dto.DeliveryFeedbackDto;
import com.ydh.helloshop.infra.amqp.dto.DeliveryMessageType;
import com.ydh.helloshop.application.domain.delivery.Delivery;
import com.ydh.helloshop.application.exception.noSuchThat.NoSuchDelivery;
import com.ydh.helloshop.application.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Profile(value = "prod")
@Component
@RequiredArgsConstructor
public class AmqpDeliveryConsumer {

    private final DeliveryRepository deliveryRepository;

    private final String queueName = "delivery2shop.queue";

    @RabbitListener(queues = {queueName})
    @Transactional
    public void consumeMessage(String deliveryJsonString) {
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
