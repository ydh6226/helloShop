package com.ydh.helloshop.domain;

import com.ydh.helloshop.item.Item;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@NoArgsConstructor
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
    protected ItemCategory(Item item, Category category) {
        this.item = item;
        this.category = category;
    }

    //setter
    public void initItem(Item item) {
        this.item = item;
    }

    //생성 메서드
    public static ItemCategory createItemCategory(Item item, Category category) {
        return new ItemCategory(item, category);
    }
}
