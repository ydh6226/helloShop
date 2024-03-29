package com.ydh.helloshop.application.domain.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ydh.helloshop.application.exception.NotEnoughStockException;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.ydh.helloshop.application.domain.item.ItemStatus.PREPARE;
import static com.ydh.helloshop.application.domain.item.ItemStatus.SALE;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.InheritanceType.SINGLE_TABLE;

@Entity
@Inheritance(strategy = SINGLE_TABLE)
@DiscriminatorColumn
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

    private String representativeImageUrl;

    @Lob
    private String description;

    private Long sellerId;

    @Column(name = "dtype", insertable = false, updatable = false)
    @Enumerated(STRING)
    private ItemType itemType;

    @JsonIgnore
    @OneToMany(mappedBy = "item", cascade = ALL)
    private List<ItemCategory> itemCategories = new ArrayList<>();

    public Item() {
        this.status = PREPARE;
        this.createTime = LocalDateTime.now();
    }

    //== 상품 정보 수정 ==//
    public void setBasicInfo(String name, int price, int stockQuantity) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public void setBasicInfo(ItemParam itemParam) {
        this.name = itemParam.getName();
        this.price = itemParam.getPrice();
        this.stockQuantity = itemParam.getStockQuantity();
        this.sellerId = itemParam.getSellerId();
        this.representativeImageUrl = itemParam.getRepresentativeImageUrl();
        this.description = itemParam.getDescription();
        addItemCategory(itemParam.getItemCategory());
    }

    //== 연관 관계 메서드 ==/
    protected void addItemCategory(ItemCategory itemCategory) {
        itemCategories.add(itemCategory);
        itemCategory.initItem(this);
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
            throw new NotEnoughStockException(name + " 상품의 재고가 부족합니다.");
        }
        stockQuantity = restStock;
    }


    public void changeStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public void updateStatusToPrepare() {
        status = PREPARE;
    }

    public void updateStatusToSale() {
        status = SALE;
    }

    public boolean canOrder(int requestStock) {
        return status == SALE && stockQuantity > requestStock;
    }
}
