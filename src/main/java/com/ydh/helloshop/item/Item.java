package com.ydh.helloshop.item;

import com.ydh.helloshop.domain.ItemCategory;
import com.ydh.helloshop.exception.NotEnoughStockException;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
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

    @OneToMany(mappedBy = "item", cascade = ALL)
    private List<ItemCategory> itemCategory;

    //상품 정보 수정
    public void setInfo(String name, int price, int stockQuantity) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

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
