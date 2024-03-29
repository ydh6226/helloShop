package com.ydh.helloshop.application.repository.item;

import com.ydh.helloshop.application.domain.item.ItemCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ItemCategoryRepository extends JpaRepository<ItemCategory, Long> {

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("delete from ItemCategory ic where ic.item.id in :ids")
    void deleteItemCategoryByIdInQuery(@Param("ids") List<Long> ids);

}
