package com.ydh.helloshop.application.service.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
public class BookDto {

    private String author;

    private String isbn;

    private String name;

    private int price;

    private int stockQuantity;

    private Long sellerId;
}
