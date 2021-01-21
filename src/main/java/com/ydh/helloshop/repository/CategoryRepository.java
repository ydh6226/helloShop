package com.ydh.helloshop.repository;

import com.ydh.helloshop.domain.Category;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findByName(String name);

    @EntityGraph(attributePaths = {"children"})
    List<Category> findByParent(Category parent);

}
