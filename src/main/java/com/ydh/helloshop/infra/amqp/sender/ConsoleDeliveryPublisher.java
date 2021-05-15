package com.ydh.helloshop.infra.amqp.sender;

import com.ydh.helloshop.infra.amqp.dto.DeliveryPublishParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Profile({"local", "test"})
@Component
public class ConsoleDeliveryPublisher implements DeliveryPublisher {
    @Override
    public void send(List<DeliveryPublishParam> deliveryPublishParams) {
        deliveryPublishParams.forEach(param -> log.info("{} 주문완료", param.getItemName()));
    }
}
