package com.ydh.helloshop.controller.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class AlbumForm {

    String artist;
    String etc;

    String name;
    int price;
    int stockQuantity;

    List<Long> categoryIds;

    public AlbumForm(String artist, String etc, String name, int price, int stockQuantity) {
        this.artist = artist;
        this.etc = etc;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }
}