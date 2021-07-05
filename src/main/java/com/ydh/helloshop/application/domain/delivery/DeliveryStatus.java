package com.ydh.helloshop.application.domain.delivery;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DeliveryStatus {
    READY("상품준비중"),
    SHIPPED("배송중"),
    COMP("배송완료");

    private final String title;

}
