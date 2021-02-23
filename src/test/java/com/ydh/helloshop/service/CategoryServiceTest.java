package com.ydh.helloshop.service;

import com.ydh.helloshop.domain.Category;
import com.ydh.helloshop.exception.noSuchThat.NoSuchCategory;
import com.ydh.helloshop.exception.NotEmptySubCategory;
import com.ydh.helloshop.repository.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static com.ydh.helloshop.domain.Category.createCategory;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class CategoryServiceTest {

    @Autowired
    EntityManager em;
    @Autowired
    CategoryService categoryService;
    @Autowired
    CategoryRepository categoryRepository;
    
    @Test
    @DisplayName("카테고리 생성")
    public void newCategory() throws Exception {
        //given
        Category parent = categoryRepository.findById(1L)
                .orElseThrow(() -> new NoSuchCategory("The category could not be found."));
        Category category = createCategory("바지", parent);

        //when
        Long categoryId = categoryService.save(category);

        //then
        int size = categoryService.findAll().size();
        assertEquals(2, size);
        assertEquals(category.getId(), categoryId);
    }

    @Test
    @DisplayName("카테고리 삭제")
    public void deleteCategory() throws Exception {
        //given
        Category parent = categoryRepository.findById(1L)
                .orElseThrow(() -> new NoSuchCategory("The category could not be found."));
        Category category1 = createCategory("바지", parent);
        Category category2 = createCategory("신발", parent);

        //when
        Long categoryId1 = categoryService.save(category1);
        Long categoryId2 = categoryService.save(category2);

        categoryService.delete(categoryId1);
//        categoryService.delete(categoryId2);

        //then
        int size = categoryRepository.findAll().size();
        assertEquals(2, size);
    }

    @Test
    @DisplayName("하위카테고리가 있는 카테고리 삭제")
    public void deleteNotEmptySubCategory() throws Exception {
        //given
        Category parent = categoryRepository.findById(1L)
                .orElseThrow(() -> new NoSuchCategory("The category could not be found."));
        Category category = createCategory("바지", parent);

        //when
        Long categoryId = categoryService.save(category);

        //then
        NotEmptySubCategory exception = assertThrows(NotEmptySubCategory.class, () -> categoryService.delete(1L));
        assertEquals("Subcategory must be moved to another category.", exception.getMessage());
    }


}