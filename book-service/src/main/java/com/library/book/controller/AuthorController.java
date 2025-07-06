package com.library.book.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.library.book.entity.Author;
import com.library.book.service.AuthorService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
public class AuthorController {

    private static final Logger log = LoggerFactory.getLogger(AuthorController.class);
    private final AuthorService authorService;

    @GetMapping
    public String listAuthors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "lastName") String sortBy,
            @RequestParam(required = false) String search,
            Model model) {

        log.info("Listing authors - page: {}, size: {}", page, size);

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortBy));
        Page<Author> authorPage;

        if (search != null && !search.isEmpty()) {
            authorPage = authorService.searchByName(search, pageRequest);
            model.addAttribute("search", search);
        } else {
            authorPage = authorService.findAll(pageRequest);
        }

        model.addAttribute("authorPage", authorPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("sortBy", sortBy);

        return "authors/list";
    }

    @GetMapping("/{id}")
    public String viewAuthor(@PathVariable Long id, Model model) {
        log.info("Viewing author with id: {}", id);

        try {
            Author author = authorService.findById(id);
            model.addAttribute("author", author);
            return "authors/view";
        } catch (Exception e) {
            log.error("Error viewing author", e);
            model.addAttribute("errorMessage", "Author not found");
            return "error/404";
        }
    }

    @GetMapping("/new")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public String showCreateForm(Model model) {
        log.info("Showing author creation form");
        model.addAttribute("author", new Author());
        return "authors/form";
    }

    @PostMapping("/new")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public String createAuthor(@Valid @ModelAttribute Author author,
                               BindingResult result,
                               Model model,
                               RedirectAttributes redirectAttributes) {

        log.info("Creating new author: {} {}", author.getFirstName(), author.getLastName());

        if (result.hasErrors()) {
            return "authors/form";
        }

        try {
            Author savedAuthor = authorService.save(author);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Author created successfully!");
            return "redirect:/authors/" + savedAuthor.getId();
        } catch (Exception e) {
            log.error("Error creating author", e);
            model.addAttribute("errorMessage", "Error creating author: " + e.getMessage());
            return "authors/form";
        }
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public String showEditForm(@PathVariable Long id, Model model) {
        log.info("Showing edit form for author: {}", id);

        try {
            Author author = authorService.findById(id);
            model.addAttribute("author", author);
            return "authors/form";
        } catch (Exception e) {
            log.error("Error loading author for edit", e);
            model.addAttribute("errorMessage", "Author not found");
            return "error/404";
        }
    }

    @PostMapping("/{id}/edit")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public String updateAuthor(@PathVariable Long id,
                               @Valid @ModelAttribute Author author,
                               BindingResult result,
                               Model model,
                               RedirectAttributes redirectAttributes) {

        log.info("Updating author: {}", id);

        if (result.hasErrors()) {
            return "authors/form";
        }

        try {
            authorService.update(id, author);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Author updated successfully!");
            return "redirect:/authors/" + id;
        } catch (Exception e) {
            log.error("Error updating author", e);
            model.addAttribute("errorMessage", "Error updating author: " + e.getMessage());
            return "authors/form";
        }
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public String deleteAuthor(@PathVariable Long id,
                               RedirectAttributes redirectAttributes) {

        log.info("Deleting author: {}", id);

        try {
            authorService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Author deleted successfully!");
        } catch (Exception e) {
            log.error("Error deleting author", e);
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error deleting author: " + e.getMessage());
        }

        return "redirect:/authors";
    }
}