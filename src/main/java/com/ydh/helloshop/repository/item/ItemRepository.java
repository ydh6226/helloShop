package com.ydh.helloshop.repository.item;

import com.ydh.helloshop.item.Item;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("select i from Item i where i.id in :itemIds")
    List<Item> findMultiple(List<Long> itemIds);
}
