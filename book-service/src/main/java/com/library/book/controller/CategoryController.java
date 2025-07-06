package com.library.book.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.book.dto.CategoryDTO;
import com.library.book.entity.Category;
import com.library.book.service.CategoryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private static final Logger log = LoggerFactory.getLogger(CategoryController.class);
    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDTO> getAllCategories() {
        log.info("Getting all categories");
        return categoryService.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        log.info("Getting category with id: {}", id);
        
        try {
            Category category = categoryService.findById(id);
            return ResponseEntity.ok(convertToDTO(category));
        } catch (Exception e) {
            log.error("Category not found with id: {}", id, e);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        log.info("Creating new category: {}", categoryDTO.getName());

        try {
            Category category = convertToEntity(categoryDTO);
            Category savedCategory = categoryService.save(category);
            return ResponseEntity.ok(convertToDTO(savedCategory));
        } catch (Exception e) {
            log.error("Error creating category", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryDTO categoryDTO) {
        log.info("Updating category: {}", id);

        try {
            Category category = convertToEntity(categoryDTO);
            category.setId(id);
            Category updatedCategory = categoryService.update(id, category);
            return ResponseEntity.ok(convertToDTO(updatedCategory));
        } catch (Exception e) {
            log.error("Error updating category", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        log.info("Deleting category: {}", id);

        try {
            categoryService.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error deleting category", e);
            return ResponseEntity.badRequest().build();
        }
    }

    private CategoryDTO convertToDTO(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        return dto;
    }

    private Category convertToEntity(CategoryDTO dto) {
        Category category = new Category();
        category.setId(dto.getId());
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        return category;
    }
}