package com.ydh.helloshop.application.controller.payment.dto;


import lombok.Data;

@Data
public class PaymentParam {
    private String impUid;
    private Long orderId;
}
