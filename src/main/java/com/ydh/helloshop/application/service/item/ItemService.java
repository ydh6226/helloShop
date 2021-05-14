package com.ydh.helloshop.application.service.item;

import com.ydh.helloshop.application.domain.item.Item;

import java.util.List;

public interface ItemService<T extends Item> {

    Long save(T item);

    void delete(Long id);

    T findOne(Long id);

    List<T> findAll();
}
