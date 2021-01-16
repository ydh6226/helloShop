package com.ydh.helloshop.repository;

import com.ydh.helloshop.domain.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.ydh.helloshop.domain.Category.createCategory;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class CategoryRepositoryTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("모든 카테고리 조회")
    public void searchAllCategory() throws Exception {
        Category root = categoryRepository.findByName("root");

        Category c1 = createCategory("의류", root);
        Category c2 = createCategory("도서", root);
        Category c11 = createCategory("상의", c1);
        Category c12 = createCategory("하의", c1);
        Category c21 = createCategory("만화", c2);
        Category c111 = createCategory("반팔", c11);

        categoryRepository.save(c1);
        categoryRepository.save(c2);
        categoryRepository.save(c11);
        categoryRepository.save(c12);
        categoryRepository.save(c21);
        categoryRepository.save(c111);

        em.flush();
        em.clear();


        List<Category> list = categoryRepository.findByParent(root);

        list.forEach(l -> l.getChildren()
                .forEach(o->o.getChildren()
                        .forEach(Category::getName)));

        System.out.println("=============================");
        System.out.println(list.size());

        list.forEach(c -> {
            System.out.println(c.getName());
            List<Category> children = c.getChildren();
            children.forEach(m -> {
                System.out.println("- " + m.getName());
                List<Category> children1 = m.getChildren();
                children1.forEach(o -> System.out.println("-- " + o.getName())); });
        });
    }

}