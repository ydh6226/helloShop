package com.ydh.helloshop.application.domain.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ydh.helloshop.application.domain.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class ItemCategory {

    @Id
    @GeneratedValue(generator = "item_category_id")
    @Column(name = "item_category_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    //생성자
    protected ItemCategory(Category category) {
        this.category = category;
    }

    public void initItem(Item item) {
        this.item = item;
    }

    //생성 메서드
    public static ItemCategory createItemCategory(Category category) {
        return new ItemCategory(category);
    }
}
