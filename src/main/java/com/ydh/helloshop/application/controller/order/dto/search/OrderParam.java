package com.ydh.helloshop.application.controller.order.dto.search;

import com.ydh.helloshop.application.domain.delivery.DeliveryStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class OrderParam {
    private Long itemId;
    private String itemName;
    private String orderDate;
    private int price;
    private DeliveryStatus deliveryStatus;

    public OrderParam(Long itemId, String itemName, LocalDateTime orderDate, int price, DeliveryStatus deliveryStatus) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.price = price;
        this.deliveryStatus = deliveryStatus;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.orderDate = orderDate.format(formatter);
    }
}