package com.ydh.helloshop.service.item;

import com.ydh.helloshop.exception.NoSuchItem;
import com.ydh.helloshop.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ItemService<T extends Item> {

    Long save(T item);

    void delete(Long id);

    T findOne(Long id);

    List<T> findAll();
}
