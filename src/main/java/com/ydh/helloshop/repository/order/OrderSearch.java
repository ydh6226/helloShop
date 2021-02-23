package com.ydh.helloshop.repository.order;

import com.ydh.helloshop.domain.order.OrderStatus;
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
