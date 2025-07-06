package com.library.book.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.library.book.entity.Category;
import com.library.book.service.CategoryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public String listCategories(Model model) {
        log.info("Listing all categories");

        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);

        return "categories/list";
    }

    @GetMapping("/{id}")
    public String viewCategory(@PathVariable Long id, Model model) {
        log.info("Viewing category with id: {}", id);

        try {
            Category category = categoryService.findById(id);
            model.addAttribute("category", category);
            return "categories/view";
        } catch (Exception e) {
            log.error("Error viewing category", e);
            model.addAttribute("errorMessage", "Category not found");
            return "error/404";
        }
    }

    @GetMapping("/new")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public String showCreateForm(Model model) {
        log.info("Showing category creation form");
        model.addAttribute("category", new Category());
        return "categories/form";
    }

    @PostMapping("/new")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public String createCategory(@Valid @ModelAttribute Category category,
                                 BindingResult result,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {

        log.info("Creating new category: {}", category.getName());

        if (result.hasErrors()) {
            return "categories/form";
        }

        try {
            Category savedCategory = categoryService.save(category);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Category created successfully!");
            return "redirect:/categories/" + savedCategory.getId();
        } catch (Exception e) {
            log.error("Error creating category", e);
            model.addAttribute("errorMessage", "Error creating category: " + e.getMessage());
            return "categories/form";
        }
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public String showEditForm(@PathVariable Long id, Model model) {
        log.info("Showing edit form for category: {}", id);

        try {
            Category category = categoryService.findById(id);
            model.addAttribute("category", category);
            return "categories/form";
        } catch (Exception e) {
            log.error("Error loading category for edit", e);
            model.addAttribute("errorMessage", "Category not found");
            return "error/404";
        }
    }

    @PostMapping("/{id}/edit")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public String updateCategory(@PathVariable Long id,
                                 @Valid @ModelAttribute Category category,
                                 BindingResult result,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {

        log.info("Updating category: {}", id);

        if (result.hasErrors()) {
            return "categories/form";
        }

        try {
            categoryService.update(id, category);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Category updated successfully!");
            return "redirect:/categories/" + id;
        } catch (Exception e) {
            log.error("Error updating category", e);
            model.addAttribute("errorMessage", "Error updating category: " + e.getMessage());
            return "categories/form";
        }
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public String deleteCategory(@PathVariable Long id,
                                 RedirectAttributes redirectAttributes) {

        log.info("Deleting category: {}", id);

        try {
            categoryService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Category deleted successfully!");
        } catch (Exception e) {
            log.error("Error deleting category", e);
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error deleting category: " + e.getMessage());
        }

        return "redirect:/categories";
    }
}