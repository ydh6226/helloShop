package com.ydh.helloshop.application.domain.delivery;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DeliveryStatus {
    READY("상품 준비중"),
    SHIPPED("배송중"),
    COMP("배송 완료");

    private final String title;

}
