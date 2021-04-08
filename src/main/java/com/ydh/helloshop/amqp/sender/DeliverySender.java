package com.ydh.helloshop.amqp.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ydh.helloshop.amqp.dto.DeliveryDelegateDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeliverySender {

    private final RabbitMessagingTemplate rabbitMessagingTemplate;

    private final String queueName = "shop2delivery.queue";
    private final String exchangeName = "shop2delivery.direct";
    private final String routingKey = "delivery-delegate";

    @Bean
    Queue queue() {
        return new Queue(queueName, false);
    }

    @Bean
    DirectExchange exchange() {
        return new DirectExchange(exchangeName);
    }

    @Bean
    Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }

    public void sendOne(DeliveryDelegateDto dto) {
        try {
            DeliveryInfoJson deliveryInfoJson = new DeliveryInfoJson();
            deliveryInfoJson.setCount(1);
            deliveryInfoJson.getDeliveryInfos().add(dto);

            String dtoJsonString = new ObjectMapper().writeValueAsString(deliveryInfoJson);
            rabbitMessagingTemplate.convertAndSend(exchangeName, routingKey, dtoJsonString);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
    }

    public void sendAll(List<DeliveryDelegateDto> dtos) {
        try {
            DeliveryInfoJson deliveryInfoJson = new DeliveryInfoJson();
            deliveryInfoJson.setCount(dtos.size());
            deliveryInfoJson.getDeliveryInfos().addAll(dtos);

            String jsonMessage = new ObjectMapper().writeValueAsString(deliveryInfoJson);
            rabbitMessagingTemplate.convertAndSend(exchangeName, routingKey, jsonMessage);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
    }

    @Data
    @NoArgsConstructor
    static class DeliveryInfoJson {
        private int count;
        private List<DeliveryDelegateDto> deliveryInfos = new ArrayList<>();
    }

}
