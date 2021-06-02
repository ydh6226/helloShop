package com.ydh.helloshop.application.controller.order.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CreateOrderParam {

    private List<RequestOrderInfo> requestOrderInfos = new ArrayList<>();
    private String shippingRequirements;
}
