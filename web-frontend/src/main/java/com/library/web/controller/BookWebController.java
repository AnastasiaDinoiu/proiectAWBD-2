package com.library.web.controller;

import com.library.web.dto.BookDTO;
import com.library.web.dto.ReviewDTO;
import com.library.web.service.BookWebService;
import com.library.web.service.ReviewWebService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
@Slf4j
public class BookWebController {

    private final BookWebService bookWebService;
    private final ReviewWebService reviewWebService;

    @GetMapping
    public String listBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(required = false) String search,
            Model model) {

        log.info("Listing books - page: {}, search: {}", page, search);

        try {
            var bookPage = bookWebService.getAllBooks(page, size, search);

            model.addAttribute("bookPage", bookPage);
            model.addAttribute("currentPage", page);
            model.addAttribute("search", search);

            return "books/list";
        } catch (Exception e) {
            log.error("Error listing books", e);
            model.addAttribute("error", "Error loading books: " + e.getMessage());
            return "books/list";
        }
    }

    @GetMapping("/{id}")
    public String viewBook(@PathVariable Long id, Model model) {
        log.info("Viewing book: {}", id);

        try {
            var book = bookWebService.getBookById(id);
            var reviews = reviewWebService.getReviewsByBook(id, 0, 10);
            var averageRating = reviewWebService.getAverageRating(id);

            model.addAttribute("book", book);
            model.addAttribute("reviews", reviews);
            model.addAttribute("averageRating", averageRating);
            model.addAttribute("newReview", new ReviewDTO());

            return "books/view";

        } catch (Exception e) {
            log.error("Error viewing book", e);
            model.addAttribute("error", "Error loading book: " + e.getMessage());
            return "books/view";
        }
    }

    @GetMapping("/new")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public String showCreateForm(Model model) {
        model.addAttribute("book", new BookDTO());
        return "books/form";
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public String showEditForm(@PathVariable Long id, Model model) {
        try {
            var book = bookWebService.getBookById(id);
            model.addAttribute("book", book);
            return "books/form";
        } catch (Exception e) {
            log.error("Error loading book for edit", e);
            return "redirect:/books";
        }
    }
}