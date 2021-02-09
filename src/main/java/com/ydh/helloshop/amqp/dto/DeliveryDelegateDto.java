package com.ydh.helloshop.amqp.dto;

import com.ydh.helloshop.domain.Address;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeliveryDelegateDto {
    private Long deliveryId;
    private String memberName;
    private String itemName;
    private Address address;
}
