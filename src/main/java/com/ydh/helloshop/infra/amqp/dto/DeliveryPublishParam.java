package com.ydh.helloshop.infra.amqp.dto;

import com.ydh.helloshop.application.domain.Address;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeliveryPublishParam {
    private Long deliveryId;
    private String memberName;
    private String itemName;
    private Address address;
}
