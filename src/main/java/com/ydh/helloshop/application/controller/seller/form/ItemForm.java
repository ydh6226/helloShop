package com.ydh.helloshop.application.controller.seller.form;

import com.ydh.helloshop.application.domain.item.ItemType;
import lombok.Data;

@Data
public class ItemForm {
    // 공통
    private ItemType itemType;
    private String categoryName;
    private String name;
    private int price;
    private int stockQuantity;
    private String description;

    //앨범
    private String artist;
    private String etc;

    //도서
    private String author;
    private String isbn;

    //가구
    private double length;
    private double width;
    private double height;
}
