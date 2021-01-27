package com.ydh.helloshop.repository;

import com.ydh.helloshop.domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    CartItem findByItemId(Long itemId);

    @Modifying(clearAutomatically = true)
    @Query("delete from CartItem ci where ci.id in :cartItemIds")
    void deleteByIdInQuery(@Param("cartItemIds") List<Long> cartItemIds);


    @Modifying(clearAutomatically = true)
    @Query("delete from CartItem ci where ci.item.id in :itemIds")
    void deleteByItemIdsInQuery(@Param("itemIds") List<Long> itemIds);
}
