package com.ydh.helloshop.infra.amqp.sender;

import com.ydh.helloshop.infra.amqp.dto.DeliveryPublishParam;

import java.util.List;

public interface DeliveryPublisher {

    void send(List<DeliveryPublishParam> deliveryPublishParams);
}
