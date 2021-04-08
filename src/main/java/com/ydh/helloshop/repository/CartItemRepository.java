package com.ydh.helloshop.repository;

import com.ydh.helloshop.domain.CartItem;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByItemId(Long itemId);

    List<CartItem> findAllByItemIdIn(@Param("itemIds") List<Long> itemIds);

    Optional<CartItem> findOneByCartIdAndItemId(Long cartId, Long itemId);

    @EntityGraph(attributePaths = {"item"})
    List<CartItem> findAllWithItemByIdIn(List<Long> ids);

    @Query("select ci from CartItem ci join fetch ci.cart join fetch ci.item where ci.id = :id")
    CartItem findByIdWithCartAndItem(@Param("id") Long id);

    @Modifying(clearAutomatically = true)
    @Query("delete from CartItem ci where ci.id in :cartItemIds")
    void deleteByIdInQuery(@Param("cartItemIds") List<Long> cartItemIds);


    @Modifying(clearAutomatically = true)
    @Query("delete from CartItem ci where ci.item.id in :itemIds")
    void deleteByItemIdsInQuery(@Param("itemIds") List<Long> itemIds);



}
