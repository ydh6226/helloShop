package com.ydh.helloshop.application.repository.item;

import com.ydh.helloshop.application.item.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}