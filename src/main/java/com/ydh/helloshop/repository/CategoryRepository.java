package com.ydh.helloshop.repository;

import com.ydh.helloshop.domain.Category;
import com.ydh.helloshop.exception.NotEmptySubCategory;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.ydh.helloshop.domain.Category.createCategory;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findByName(String name);

    @EntityGraph(attributePaths = {"children"})
    List<Category> findByParent(Category parent);

}
