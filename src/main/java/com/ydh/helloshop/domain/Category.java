package com.ydh.helloshop.domain;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
public class Category {

    @Id
    @GeneratedValue(generator = "category_id")
    @Column(name = "category_id")
    private Long id;

    private String name;

//    @OneToMany(mappedBy = "category")
//    private List<ItemCategory> itemCategories = new ArrayList<>();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> children = new ArrayList<>();
}
