package com.ydh.helloshop.application.controller.order.dto;

import com.ydh.helloshop.application.repository.order.dto.OrderParam;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class OrderSearchResParam {

    private PageMetaData pageMetaData;
    private List<OrderParam> orderParams = new ArrayList<>();

    public OrderSearchResParam(PageMetaData pageMetaData, List<OrderParam> orderParams) {
        this.pageMetaData = pageMetaData;
        this.orderParams = orderParams;
    }
}
