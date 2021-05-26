package com.ydh.helloshop.application.repository.item;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ydh.helloshop.application.controller.item.ItemSearch;
import com.ydh.helloshop.application.domain.item.Album;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import javax.persistence.EntityManager;
import java.util.List;

import static com.ydh.helloshop.application.domain.QCategory.category;
import static com.ydh.helloshop.application.domain.item.QAlbum.album;
import static com.ydh.helloshop.application.domain.item.QItemCategory.itemCategory;

public class AlbumRepositoryImpl extends QuerydslRepositorySupport implements AlbumRepositoryCustom {

    private final JPAQueryFactory query;

    public AlbumRepositoryImpl(EntityManager em) {
        super(Album.class);
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public List<Album> findAlbumWihSearch(ItemSearch itemSearch) {
        return query.select(album)
                .from(album)
                .join(album.itemCategories, itemCategory)
                .join(itemCategory.category, category)
                .where(categoryEq(itemSearch.getCategoryName()),
                        itemNameLike(itemSearch.getItemName()))
                .distinct()
                .fetch();
    }

    private BooleanExpression categoryEq(String categoryName) {
        if(categoryName.length() == 0) {
            return null;
        }
        return category.name.eq(categoryName);
    }

    private BooleanExpression itemNameLike(String itemName) {
        if (itemName == null) {
            return null;
        }
        return album.name.contains(itemName);
    }
}
