package com.ydh.helloshop.repository;

import com.ydh.helloshop.domain.Category;
import com.ydh.helloshop.exception.NotEmptySubCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

import static com.ydh.helloshop.domain.Category.createCategory;

@Repository
@RequiredArgsConstructor
public class CategoryRepository {

    private final EntityManager em;

    public Long save(Category category) {
        em.persist(category);
        return category.getId();
    }

    public void update(Long categoryId, String name, Long parentId){
        Category category = em.find(Category.class, categoryId);
        category.setInfo(name, em.find(Category.class, parentId));
    }

    public Category findOne(Long id){
        return em.find(Category.class, id);
    }

    public List<Category> findAll(){
        return em.createQuery("select c from Category c", Category.class)
                .getResultList();
    }

    public void delete(Long id){
        Category category = em.find(Category.class, id);
        if (category.getChildren().size() != 0){
            throw new NotEmptySubCategory("Subcategory must be moved to another category!!");
        }
        em.remove(category);
    }

    public void createRootCategory() {
        Category category = createCategory("root", null);
        em.persist(category);
    }
}
