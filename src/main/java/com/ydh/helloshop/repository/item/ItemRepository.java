package com.ydh.helloshop.repository.item;

import com.ydh.helloshop.item.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("select i from Item i where i.id in :itemIds")
    List<Item> findMultiple(@Param("itemIds") List<Long> itemIds);

    @Query("select distinct i from Item i join fetch i.itemCategories ic join fetch ic.category where i.sellerId = :sellerId")
    List<Item> findAllBySellerIdWithItemCategory(@Param("sellerId") Long sellerId);

    @Modifying(clearAutomatically = true)
    @Query("delete from Item i where i.id in :itemIds")
    void deleteItemsByIdIn(@Param("itemIds") List<Long> itemIds);

    @Query("select i from Item i")
    Page<Item> findAllWithPaging(Pageable pageable);
}
