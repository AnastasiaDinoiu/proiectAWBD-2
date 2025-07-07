package com.library.web.controller;

import com.library.web.dto.CategoryDTO;
import com.library.web.service.CategoryWebService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryWebController {

    private final CategoryWebService categoryWebService;

    @GetMapping
    public String listCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection,
            @RequestParam(required = false) String search,
            Model model) {

        log.info("Listing categories - page: {}, size: {}", page, size);

        try {
            var categoryPage = categoryWebService.getAllCategories(page, size, sortBy, sortDirection, search);

            model.addAttribute("categoryPage", categoryPage);
            model.addAttribute("categories", categoryPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("sortBy", sortBy);
            model.addAttribute("sortDirection", sortDirection);
            model.addAttribute("searchTerm", search);

            return "categories/list";

        } catch (Exception e) {
            log.error("Error listing categories", e);
            model.addAttribute("error", "Error loading categories: " + e.getMessage());
            return "categories/list";
        }
    }

    @GetMapping("/{id}")
    public String viewCategory(@PathVariable Long id, Model model) {
        log.info("Viewing category: {}", id);

        try {
            CategoryDTO category = categoryWebService.getCategoryById(id);
            model.addAttribute("category", category);
            return "categories/view";

        } catch (Exception e) {
            log.error("Error viewing category: {}", id, e);
            return "redirect:/categories";
        }
    }

    @GetMapping("/new")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public String showCreateForm(Model model) {
        log.info("Showing category creation form");
        model.addAttribute("category", new CategoryDTO());
        return "categories/form";
    }

    @PostMapping("/new")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public String createCategory(@ModelAttribute CategoryDTO category,
                                 RedirectAttributes redirectAttributes) {
        log.info("Creating new category: {}", category.getName());

        try {
            CategoryDTO savedCategory = categoryWebService.createCategory(category);
            redirectAttributes.addFlashAttribute("successMessage", "Category created successfully!");
            return "redirect:/categories/" + savedCategory.getId();

        } catch (Exception e) {
            log.error("Error creating category", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating category: " + e.getMessage());
            return "redirect:/categories/new";
        }
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public String showEditForm(@PathVariable Long id, Model model) {
        log.info("Showing edit form for category: {}", id);

        try {
            CategoryDTO category = categoryWebService.getCategoryById(id);
            model.addAttribute("category", category);
            return "categories/form";

        } catch (Exception e) {
            log.error("Error loading category for edit: {}", id, e);
            return "redirect:/categories";
        }
    }

    @PostMapping("/{id}/edit")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public String updateCategory(@PathVariable Long id,
                                 @ModelAttribute CategoryDTO category,
                                 RedirectAttributes redirectAttributes) {
        log.info("Updating category: {}", id);

        try {
            categoryWebService.updateCategory(id, category);
            redirectAttributes.addFlashAttribute("successMessage", "Category updated successfully!");
            return "redirect:/categories/" + id;

        } catch (Exception e) {
            log.error("Error updating category", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating category: " + e.getMessage());
            return "redirect:/categories/" + id + "/edit";
        }
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        log.info("Deleting category: {}", id);

        try {
            categoryWebService.deleteCategory(id);
            redirectAttributes.addFlashAttribute("successMessage", "Category deleted successfully!");

        } catch (Exception e) {
            log.error("Error deleting category", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting category: " + e.getMessage());
        }

        return "redirect:/categories";
    }
}