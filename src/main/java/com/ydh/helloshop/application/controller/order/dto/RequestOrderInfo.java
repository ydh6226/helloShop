package com.ydh.helloshop.application.controller.order.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RequestOrderInfo {

    private int count;
    private Long itemId;

    public RequestOrderInfo(int count, Long itemId) {
        this.count = count;
        this.itemId = itemId;
    }
}
