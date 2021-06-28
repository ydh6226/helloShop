package com.ydh.helloshop.application.repository.item;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ydh.helloshop.application.controller.item.ItemSearch;
import com.ydh.helloshop.application.domain.QCategory;
import com.ydh.helloshop.application.domain.item.Item;
import com.ydh.helloshop.application.domain.item.ItemStatus;
import com.ydh.helloshop.application.domain.item.QItemCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

import static com.ydh.helloshop.application.domain.QCategory.category;
import static com.ydh.helloshop.application.domain.item.QItem.item;
import static com.ydh.helloshop.application.domain.item.QItemCategory.itemCategory;

public class ItemRepositoryImpl implements CustomItemRepository {

    private final JPAQueryFactory query;

    public ItemRepositoryImpl(EntityManager em) {
        query = new JPAQueryFactory(em);
    }

    @Override
    public Page<Item> findOnSaleItemsWithPaging(Pageable pageable) {
        QueryResults<Item> results = query.selectFrom(item)
                .where(item.status.eq(ItemStatus.SALE))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    @Override
    public List<Item> findItemsBySearch(ItemSearch search) {
        return query.selectFrom(item)
                .join(item.itemCategories, itemCategory)
                .join(itemCategory.category, category)
                .where(categoryEq(search.getCategoryName()))
                .where(itemNameEq(search.getItemName()))
                .distinct()
                .fetch();
    }

    private BooleanExpression categoryEq(String categoryName) {
        if (categoryName.isEmpty()) {
            return null;
        }
        return itemCategory.category.name.eq(categoryName);
    }

    private BooleanExpression itemNameEq(String itemName) {
        if (itemName.isEmpty()) {
            return null;
        }
        return item.name.containsIgnoreCase(itemName);
    }
}
