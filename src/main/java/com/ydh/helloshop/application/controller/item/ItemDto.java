package com.ydh.helloshop.application.controller.item;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemDto {

    private Long id;
    private String name;
    private String representativeImageUrl;
    private int price;
}
