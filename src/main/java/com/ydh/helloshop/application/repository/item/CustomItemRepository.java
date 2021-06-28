package com.ydh.helloshop.application.repository.item;

import com.ydh.helloshop.application.controller.item.ItemSearch;
import com.ydh.helloshop.application.domain.item.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomItemRepository {

    Page<Item> findOnSaleItemsWithPaging(Pageable pageable);

    List<Item> findItemsBySearch(ItemSearch search);
}
