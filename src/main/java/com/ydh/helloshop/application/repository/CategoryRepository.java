package com.ydh.helloshop.application.repository;

import com.ydh.helloshop.application.domain.Category;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findByName(String name);

    @EntityGraph(attributePaths = {"children"})
    List<Category> findByParent(Category parent);

    @Query("select c3 from Category c0" +
            " join c0.children c1" +
            " join c1.children c2" +
            " join c2.children c3")
    List<Category> findLeafCategories();
}
