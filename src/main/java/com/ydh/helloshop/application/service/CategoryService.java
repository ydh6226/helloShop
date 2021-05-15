package com.ydh.helloshop.application.service;

import com.ydh.helloshop.application.controller.admin.form.CategoryForm;
import com.ydh.helloshop.application.domain.Category;
import com.ydh.helloshop.application.exception.NotEmptySubCategory;
import com.ydh.helloshop.application.exception.noSuchThat.NoSuchCategory;
import com.ydh.helloshop.application.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ydh.helloshop.application.domain.Category.createCategory;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> findLeafCategories() {
        return categoryRepository.findLeafCategories();
    }

    @Transactional
    public Long save(Category category) {
        categoryRepository.save(category);
        return category.getId();
    }

    @Transactional
    public void update(Long categoryId, String name, Long parentId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NoSuchCategory("The category could not be found."));

        Category parentCategory = categoryRepository.findById(parentId)
                .orElseThrow(() -> new NoSuchCategory("The category could not be found."));

        category.changeInfo(name, parentCategory);
    }

    @Transactional
    public void delete(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NoSuchCategory("The category could not be found."));

        if (category.getChildren().size() != 0){
            throw new NotEmptySubCategory("Subcategory must be moved to another category.");
        }
        categoryRepository.deleteById(id);
    }

    public Category findOne(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NoSuchCategory("The category could not be found."));
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public List<Category> findAllById(List<Long> categoryIds) {
        return categoryRepository.findAllById(categoryIds);
    }

    public List<Category> findAllByKind() {
        Category root = categoryRepository.findByName("root");
        List<Category> list = categoryRepository.findByParent(root);

        list.forEach(p -> p.getChildren()
                .forEach(c -> c.getChildren()
                        .forEach(Category::getName)));

        return list;
    }

    @Transactional
    public void create(CategoryForm form) {
        Category parentCategory = categoryRepository.findById(form.getParentId())
                .orElseThrow(() -> new NoSuchCategory("The parent category could not be found."));

        categoryRepository.save(createCategory(form.getName(), parentCategory));
    }


}
