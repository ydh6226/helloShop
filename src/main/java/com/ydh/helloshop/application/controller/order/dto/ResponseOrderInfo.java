package com.ydh.helloshop.application.controller.order.dto;

import com.ydh.helloshop.application.domain.item.Item;
import lombok.Data;

@Data
public class ResponseOrderInfo {

    private int count;
    private Item item;
    private int totalPrice;

    public ResponseOrderInfo(int count, Item item, int totalPrice) {
        this.count = count;
        this.item = item;
        this.totalPrice = totalPrice;
    }
}
