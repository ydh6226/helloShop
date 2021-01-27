package com.ydh.helloshop.service;

import com.ydh.helloshop.controller.admin.category.CategoryForm;
import com.ydh.helloshop.domain.Category;
import com.ydh.helloshop.exception.NoSuchCategory;
import com.ydh.helloshop.exception.NotEmptySubCategory;
import com.ydh.helloshop.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ydh.helloshop.domain.Category.createCategory;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

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
