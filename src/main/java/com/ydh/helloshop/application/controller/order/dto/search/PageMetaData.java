package com.ydh.helloshop.application.controller.order.dto.search;

import lombok.Data;

@Data
public class PageMetaData {

    private int pageNumber;
    private int pageSize;
    private int pageCount;
    private Long totalCount;

    public PageMetaData(int pageNumber, int pageSize, int pageCount, Long totalCount) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.pageCount = pageCount;
        this.totalCount = totalCount;
    }
}