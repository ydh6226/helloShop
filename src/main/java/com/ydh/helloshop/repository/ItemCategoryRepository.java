package com.ydh.helloshop.repository;

import com.ydh.helloshop.domain.ItemCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *   SellerController::deleteItem 에 종속적으로 사용되기 때문에
 *   Service단 없이 Repository 직접 사용
 */
public interface ItemCategoryRepository extends JpaRepository<ItemCategory, Long> {

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("delete from ItemCategory ic where ic.item.id in :ids")
    void deleteItemCategoryByIdInQuery(List<Long> ids);
}
