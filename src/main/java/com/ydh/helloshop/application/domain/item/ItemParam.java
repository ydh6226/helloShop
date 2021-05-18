package com.ydh.helloshop.application.domain.item;

import lombok.Data;

@Data
public class ItemParam {

    private String name;
    private int price;
    private int stockQuantity;
    private Long sellerId;
    ItemCategory itemCategory;
}
