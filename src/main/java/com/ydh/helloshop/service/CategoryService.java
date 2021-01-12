package com.ydh.helloshop.service;

import com.ydh.helloshop.domain.Category;
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
        return categoryRepository.save(category);
    }

    @Transactional
    public void update(Long categoryId, String name, Long parentId) {
        categoryRepository.update(categoryId, name, parentId);
    }

    @Transactional
    public void delete(Long id) {
        categoryRepository.delete(id);
    }

    public Category findOne(Long id) {
        return categoryRepository.findOne(id);
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }
}
