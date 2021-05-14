package com.ydh.helloshop.application.service.item;

import com.ydh.helloshop.application.domain.item.ItemCategory;
import com.ydh.helloshop.application.domain.item.Book;
import com.ydh.helloshop.application.repository.CategoryRepository;
import com.ydh.helloshop.application.repository.item.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BookService implements ItemService<Book> {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Long save(Book book) {
        return bookRepository.save(book).getId();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public Book findOne(Long id) {
        return bookRepository.findById(id).get();
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Transactional
    public Long create(BookDto dto, List<ItemCategory> itemCategories) {
        Book book = Book.createBook(dto, itemCategories);
        bookRepository.save(book);
        return book.getId();
    }
}
