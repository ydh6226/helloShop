package com.ydh.helloshop.item;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("B")
@NoArgsConstructor
@Getter
public class Book extends Item{

    private String author;
    private String isbn;

    public Book(String author, String isbn, String name, int price, int stockQuantity) {
        this.author = author;
        this.isbn = isbn;
        super.changeInfo(name, price, stockQuantity);
    }
}
