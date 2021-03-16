package com.ydh.helloshop.repository.item;

import com.ydh.helloshop.item.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
