package com.ydh.helloshop.controller.item;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class AlbumForm {

    private String artist;
    private String etc;

    private String name;
    private int price;
    private int stockQuantity;

    private Long sellerId;

    private List<Long> categoryIds = new ArrayList<>();

    public AlbumForm(Long sellerId, String artist, String etc, String name, int price, int stockQuantity) {
        this.sellerId = sellerId;
        this.artist = artist;
        this.etc = etc;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }
}