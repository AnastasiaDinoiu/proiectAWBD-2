package com.library.book.controller;

import com.library.book.entity.Book;
import com.library.book.service.BookService;
import com.library.book.service.CategoryService;
import com.library.book.service.PublisherService;
import com.library.book.service.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private static final Logger log = LoggerFactory.getLogger(BookController.class);
    private final BookService bookService;
    private final CategoryService categoryService;
    private final PublisherService publisherService;
    private final AuthorService authorService;

    @GetMapping
    public String listBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection,
            @RequestParam(required = false) String search,
            Model model) {

        log.info("Listing books - page: {}, size: {}, sortBy: {}, sortDirection: {}",
                page, size, sortBy, sortDirection);

        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC")
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<Book> bookPage;
        if (search != null && !search.isEmpty()) {
            bookPage = bookService.searchByTitle(search.trim(), pageRequest);
            model.addAttribute("search", search);
        } else {
            bookPage = bookService.findAll(pageRequest);
        }

        model.addAttribute("bookPage", bookPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDirection", sortDirection);

        return "books/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        log.info("Showing book creation form");

        model.addAttribute("book", new Book());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("publishers", publisherService.findAll());
        model.addAttribute("authors", authorService.findAll());

        return "books/form";
    }

    @PostMapping("/new")
    public String createBook(@Valid @ModelAttribute Book book,
                             BindingResult result,
                             Model model,
                             RedirectAttributes redirectAttributes) {

        log.info("Creating new book: {}", book.getTitle());

        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("publishers", publisherService.findAll());
            model.addAttribute("authors", authorService.findAll());
            return "books/form";
        }

        try {
            Book savedBook = bookService.save(book);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Book created successfully!");
            return "redirect:/books/" + savedBook.getId();
        } catch (Exception e) {
            log.error("Error creating book", e);
            model.addAttribute("errorMessage", "Error creating book: " + e.getMessage());
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("publishers", publisherService.findAll());
            model.addAttribute("authors", authorService.findAll());
            return "books/form";
        }
    }

    @GetMapping("/{id}")
    public String viewBook(@PathVariable Long id, Model model) {
        log.info("Viewing book with id: {}", id);

        try {
            Book book = bookService.findById(id);
            model.addAttribute("book", book);
            return "books/view";
        } catch (Exception e) {
            log.error("Error viewing book", e);
            model.addAttribute("errorMessage", "Book not found");
            return "error/404";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        log.info("Showing edit form for book: {}", id);

        try {
            Book book = bookService.findById(id);
            model.addAttribute("book", book);
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("publishers", publisherService.findAll());
            model.addAttribute("authors", authorService.findAll());
            return "books/form";
        } catch (Exception e) {
            log.error("Error loading book for edit", e);
            model.addAttribute("errorMessage", "Book not found");
            return "error/404";
        }
    }

    @PostMapping("/{id}/edit")
    public String updateBook(@PathVariable Long id,
                             @Valid @ModelAttribute Book book,
                             BindingResult result,
                             Model model,
                             RedirectAttributes redirectAttributes) {

        log.info("Updating book: {}", id);

        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("publishers", publisherService.findAll());
            model.addAttribute("authors", authorService.findAll());
            return "books/form";
        }

        try {
            bookService.update(id, book);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Book updated successfully!");
            return "redirect:/books/" + id;
        } catch (Exception e) {
            log.error("Error updating book", e);
            model.addAttribute("errorMessage", "Error updating book: " + e.getMessage());
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("publishers", publisherService.findAll());
            model.addAttribute("authors", authorService.findAll());
            return "books/form";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteBook(@PathVariable Long id,
                             RedirectAttributes redirectAttributes) {

        log.info("Deleting book: {}", id);

        try {
            bookService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Book deleted successfully!");
        } catch (Exception e) {
            log.error("Error deleting book", e);
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error deleting book: " + e.getMessage());
        }

        return "redirect:/books";
    }
}