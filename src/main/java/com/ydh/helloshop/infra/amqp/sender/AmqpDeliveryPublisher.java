package com.ydh.helloshop.infra.amqp.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ydh.helloshop.infra.amqp.dto.DeliveryPublishParam;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Profile({"prod", "dev"})
@Slf4j
@Component
@RequiredArgsConstructor
public class AmqpDeliveryPublisher implements DeliveryPublisher {

    private final RabbitMessagingTemplate rabbitMessagingTemplate;

    private final AmqpProperty amqpProperty;

    @Bean
    Queue queue() {
        return new Queue(amqpProperty.getQueueName(), false);
    }

    @Bean
    DirectExchange exchange() {
        return new DirectExchange(amqpProperty.getExchangeName());
    }

    @Bean
    Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(amqpProperty.getRoutingKey());
    }

    @Override
    public void send(List<DeliveryPublishParam> deliveryPublishParams) {
        try {
            rabbitMessagingTemplate.convertAndSend(amqpProperty.getExchangeName(), amqpProperty.getRoutingKey(),
                    new ObjectMapper().writeValueAsString(new DeliveryInfoJson(deliveryPublishParams)));
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("객체를 json 형식으로 변환 할 수 없습니다.");
        }
    }

    @Data
    @AllArgsConstructor
    static class DeliveryInfoJson {
        private int count;
        private List<DeliveryPublishParam> deliveryInfos = new ArrayList<>();

        public DeliveryInfoJson(List<DeliveryPublishParam> deliveryInfos) {
            count = deliveryInfos.size();
            this.deliveryInfos = deliveryInfos;
        }
    }
}
