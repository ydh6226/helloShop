package com.ydh.helloshop.application.controller.order.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class RequestOrderParam {

    private List<RequestOrderInfo> requestOrderInfos = new ArrayList<>();
}
