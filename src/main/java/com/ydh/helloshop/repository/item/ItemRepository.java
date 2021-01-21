package com.ydh.helloshop.repository.item;

import com.ydh.helloshop.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("select i from Item i where i.id in :itemIds")
    List<Item> findMultiple(List<Long> itemIds);

    List<Item> findAllBySellerId(Long id);

    @Modifying(clearAutomatically = true)
    @Query("delete from Item i where i.id in :itemIds")
    void deleteItemsByIdIn(List<Long> itemIds);
}
