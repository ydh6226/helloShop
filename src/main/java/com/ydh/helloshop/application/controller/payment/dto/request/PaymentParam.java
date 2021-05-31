package com.ydh.helloshop.application.controller.payment.dto.request;


import lombok.Data;

@Data
public class PaymentParam {
    private String impUid;
    private Long orderId;
}
