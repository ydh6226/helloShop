package com.ydh.helloshop.application.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_id_gen")
    @SequenceGenerator(name = "category_id_gen", sequenceName = "category_id")
    @Column(name = "category_id")
    private Long id;

    private String name;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> children = new ArrayList<>();


    //setter
    public void changeInfo(String name, Category parent) {
        this.name = name;
        if (parent != null) {
            this.setParent(parent);
        }
    }

    //== 연관 관계 메서드 ==/
    private void setParent(Category parent){
        this.parent = parent;
        parent.getChildren().add(this);
    }

    //카테고리 생성 메서드
    public static Category createCategory(String name, Category parent) {
        Category category = new Category();
        category.changeInfo(name, parent);
        return category;
    }
}
