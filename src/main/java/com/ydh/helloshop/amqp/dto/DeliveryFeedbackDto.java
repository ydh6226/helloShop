package com.ydh.helloshop.amqp.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryFeedbackDto {

    private Long deliveryId;
    private DeliveryMessageType messageType;
}
