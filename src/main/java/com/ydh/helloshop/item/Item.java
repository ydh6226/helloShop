package com.ydh.helloshop.item;

import com.ydh.helloshop.domain.ItemCategory;
import com.ydh.helloshop.exception.NotEnoughStockException;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.ydh.helloshop.item.ItemStatus.SALE;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.InheritanceType.SINGLE_TABLE;

@Entity
@Inheritance(strategy = SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter
public abstract class Item {

    @Id
    @GeneratedValue(generator = "item_id")
    @Column(name = "item_id")
    private Long id;

    private String name;

    private int price;

    private int stockQuantity;

    @Enumerated(STRING)
    private ItemStatus status;

    private LocalDateTime createTime;

    //불변 값 설정: 판매자 아이디는 바꿀 수 없다.
//    @JoinColumn(foreignKey = )
    private final Long sellerId;

    @OneToMany(mappedBy = "item", cascade = ALL)
    private List<ItemCategory> itemCategories = new ArrayList<>();

    //생성자
    protected Item() {
        sellerId = 0L;
    }

    public Item(Long sellerId) {
        this.status = SALE;
        this.sellerId = sellerId;
        this.createTime = LocalDateTime.now();
    }

    //== 상품 정보 수정 ==//
    public void changeInfo(String name, int price, int stockQuantity) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    //== 연관 관계 메서드 ==/
    protected void addItemCategory(ItemCategory itemCategory) {
        itemCategories.add(itemCategory);
        itemCategory.changeItem(this);
    }

    //== 비즈니스 로직 ==/
    //재고 증가
    public void addStock(int count) {
        stockQuantity += count;
    }

    //재고 감소
    public void removeStock(int count) {
        int restStock = stockQuantity - count;
        if (restStock < 0) {
            throw new NotEnoughStockException("need more stock!!");
        }
        stockQuantity = restStock;
    }
}
