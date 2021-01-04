package com.ydh.helloshop.repository;

import com.ydh.helloshop.domain.OrderStatus;
import lombok.Getter;

@Getter
public class OrderSearch {

    private Long memberId;
    private String itemName;
    private OrderStatus orderStatus;
}
