package com.ydh.helloshop.repository;

import com.ydh.helloshop.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public Long save(Item item) {
        if (item.getId() == null){
            em.persist(item);
        } else {
            em.merge(item);
        }
        return item.getId();
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }

    public List<Item> findSelectedItem(List<Long> itemIds) {
        return em.createQuery("select i from Item i where i in :items", Item.class)
                .setParameter("items", itemIds)
                .getResultList();
    }
}
