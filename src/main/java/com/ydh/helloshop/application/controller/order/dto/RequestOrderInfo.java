package com.ydh.helloshop.application.controller.order.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class RequestOrderInfo {

    private int count;
    private Long itemId;
}
