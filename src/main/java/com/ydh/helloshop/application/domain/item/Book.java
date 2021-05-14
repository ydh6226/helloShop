package com.ydh.helloshop.application.domain.item;

import com.ydh.helloshop.application.service.item.BookDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.List;

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

    public Book(Long sellerId, String author, String isbn, String name, int price, int stockQuantity) {
        this.author = author;
        this.isbn = isbn;
        super.changeInfo(name, price, stockQuantity);
    }

    //setter
    public void changeBookInfo(String author, String isbn) {
        this.author = author;
        this.isbn = isbn;
    }

    public static Book createBook(BookDto dto, List<ItemCategory> itemCategories) {
        Book book = new Book(dto.getSellerId(), dto.getAuthor(), dto.getIsbn(), dto.getName(),
                dto.getPrice(), dto.getStockQuantity());
        itemCategories.forEach(book::addItemCategory);
        return book;
    }
}
