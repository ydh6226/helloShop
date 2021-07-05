package com.ydh.helloshop.application.repository.order.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.ydh.helloshop.application.domain.delivery.DeliveryStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class OrderParam {
    private Long itemId;
    private String itemName;
    private String orderDate;
    private int count;
    private int totalPrice;
    private DeliveryStatus deliveryStatus;

    @QueryProjection
    public OrderParam(Long itemId, String itemName, LocalDateTime orderDate, int price, int count, DeliveryStatus deliveryStatus) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.count = count;
        this.totalPrice = price * count;
        this.deliveryStatus = deliveryStatus;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.orderDate = orderDate.format(formatter);
    }
}