package com.ydh.helloshop.controller.seller;

import lombok.Data;

import java.util.List;

@Data
public class AlbumForm {
    private String name;

    private int price;

    private int stockQuantity;

    private Long sellerId;

    private String artist;

    private String etc;

    private List<Long> categoryIds;
}
