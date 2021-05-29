package com.ydh.helloshop.application.controller.order.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ResponseOrderParam {

    List<ResponseOrderInfo> responseOrderParams = new ArrayList<>();
    int totalPrice;

    public void addParam(ResponseOrderInfo responseOrderInfo) {
        responseOrderParams.add(responseOrderInfo);
        totalPrice += responseOrderInfo.getTotalPrice();
    }
}
