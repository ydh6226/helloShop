package com.ydh.helloshop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.sql.Delete;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id
    @GeneratedValue(generator = "category_id")
    @Column(name = "category_id")
    private Long id;

    private String name;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> children = new ArrayList<>();


    //setter
    public void setInfo(String name, Category parent) {
        this.name = name;
        if (parent != null) {
            this.setParent(parent);
        }
    }

    //== 연관관계 메서드 ==//
    private void setParent(Category parent){
        this.parent = parent;
        parent.getChildren().add(this);
    }

    //카테고리 생성 메서드
    public static Category createCategory(String name, Category parent) {
        Category category = new Category();
        category.setInfo(name, parent);
        return category;
    }
}
