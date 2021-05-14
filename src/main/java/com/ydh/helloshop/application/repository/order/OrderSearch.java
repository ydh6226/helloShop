package com.ydh.helloshop.application.repository.order;

import com.ydh.helloshop.application.domain.order.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrderSearch {

    private Long memberId;
    private String itemName;
    private OrderStatus orderStatus;

    public OrderSearch(Long memberId) {
        this.memberId = memberId;
    }
}
