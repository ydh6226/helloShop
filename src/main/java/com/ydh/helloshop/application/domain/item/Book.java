package com.ydh.helloshop.application.domain.item;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import static com.ydh.helloshop.application.domain.item.ItemType.Values.BOOK;

@Entity
@DiscriminatorValue(BOOK)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Book extends Item{

    private String author;
    private String isbn;

    public Book(String author, String isbn) {
        this.author = author;
        this.isbn = isbn;
    }
}
