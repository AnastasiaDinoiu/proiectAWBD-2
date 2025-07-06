package com.library.book.service;

import com.library.book.entity.Category;
import com.library.book.exception.ResourceNotFoundException;
import com.library.book.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private static final Logger log = LoggerFactory.getLogger(CategoryService.class);
    private final CategoryRepository categoryRepository;

    public List<Category> findAll() {
        log.debug("Finding all categories");
        return categoryRepository.findAll();
    }

    public Category findById(Long id) {
        log.debug("Finding category by id: {}", id);
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }

    public Category save(Category category) {
        log.debug("Saving category: {}", category.getName());

        if (categoryRepository.existsByName(category.getName())) {
            throw new IllegalArgumentException("Category already exists with name: " + category.getName());
        }

        return categoryRepository.save(category);
    }

    public Category update(Long id, Category category) {
        log.debug("Updating category with id: {}", id);
        Category existingCategory = findById(id);

        if (!existingCategory.getName().equals(category.getName()) &&
                categoryRepository.existsByName(category.getName())) {
            throw new IllegalArgumentException("Category already exists with name: " + category.getName());
        }

        existingCategory.setName(category.getName());
        existingCategory.setDescription(category.getDescription());

        return categoryRepository.save(existingCategory);
    }

    public void deleteById(Long id) {
        log.debug("Deleting category with id: {}", id);
        Category category = findById(id);

        if (!category.getBooks().isEmpty()) {
            throw new IllegalStateException("Cannot delete category with associated books");
        }

        categoryRepository.delete(category);
    }
}