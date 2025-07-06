package com.library.web.controller;

import com.library.web.dto.BookDTO;
import com.library.web.dto.BookPageDTO;
import com.library.web.dto.ReviewDTO;
import com.library.web.service.BookWebService;
import com.library.web.service.ReviewWebService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookWebController {

    private final BookWebService bookWebService;
    private final ReviewWebService reviewWebService;

    @GetMapping
    public String listBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            Model model) {
        
        try {
            log.info("Listing books - page: {}, search: {}", page, search);
            
            BookPageDTO bookPage = bookWebService.getAllBooks(page, size, search);
            
            if (bookPage == null) {
                log.warn("BookWebService returned null, creating empty page");
                bookPage = createEmptyPage();
            }
            
            model.addAttribute("bookPage", bookPage);
            model.addAttribute("currentPage", page);
            model.addAttribute("search", search);
            
            log.info("Successfully loaded {} books", 
                    bookPage.getContent() != null ? bookPage.getContent().size() : 0);
            
        } catch (Exception e) {
            log.error("Error listing books", e);
            model.addAttribute("bookPage", createEmptyPage());
            model.addAttribute("error", "Nu s-au putut încărca cărțile. Verifică dacă serviciul de cărți rulează.");
        }
        
        return "books/list";
    }

    @GetMapping("/new")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public String showCreateForm(Model model) {
        BookDTO book = new BookDTO();
        // Setează valori default pentru a evita erorile de validare
        book.setAvailableCopies(1);
        book.setPages(100);
        book.setPrice(BigDecimal.valueOf(29.99));
        model.addAttribute("book", book);
        return "books/form";
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public String createBook(@Valid @ModelAttribute BookDTO book, 
                            BindingResult result, 
                            Model model,
                            RedirectAttributes redirectAttributes) {
        try {
            if (result.hasErrors()) {
                model.addAttribute("book", book);
                return "books/form";
            }

            BookDTO savedBook = bookWebService.saveBook(book);
            redirectAttributes.addFlashAttribute("message", "Cartea a fost adăugată cu succes!");
            return "redirect:/books/" + savedBook.getId();
            
        } catch (Exception e) {
            log.error("Error creating book", e);
            model.addAttribute("book", book);
            model.addAttribute("error", "Eroare la salvarea cărții: " + e.getMessage());
            return "books/form";
        }
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public String showEditForm(@PathVariable Long id, Model model) {
        try {
            BookDTO book = bookWebService.getBookById(id);
            model.addAttribute("book", book);
            return "books/form";
        } catch (Exception e) {
            log.error("Error loading book for edit", e);
            return "redirect:/books";
        }
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public String updateBook(@PathVariable Long id,
                            @Valid @ModelAttribute BookDTO book,
                            BindingResult result,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        try {
            if (result.hasErrors()) {
                book.setId(id);
                model.addAttribute("book", book);
                return "books/form";
            }

            book.setId(id);
            bookWebService.updateBook(id, book);
            redirectAttributes.addFlashAttribute("message", "Cartea a fost actualizată cu succes!");
            return "redirect:/books/" + id;
            
        } catch (Exception e) {
            log.error("Error updating book", e);
            book.setId(id);
            model.addAttribute("book", book);
            model.addAttribute("error", "Eroare la actualizarea cărții: " + e.getMessage());
            return "books/form";
        }
    }

    @GetMapping("/{id}")
    public String viewBook(@PathVariable Long id, Model model) {
        log.info("Viewing book: {}", id);

        try {
            BookDTO book = bookWebService.getBookById(id);
            model.addAttribute("book", book);
            
            // Încearcă să încarce recenziile, dar nu eșua dacă nu funcționează
            try {
                var reviews = reviewWebService.getReviewsByBook(id, 0, 10);
                var averageRating = reviewWebService.getAverageRating(id);
                model.addAttribute("reviews", reviews);
                model.addAttribute("averageRating", averageRating);
            } catch (Exception e) {
                log.warn("Could not load reviews for book {}: {}", id, e.getMessage());
                model.addAttribute("reviews", java.util.Collections.emptyList());
                model.addAttribute("averageRating", 0.0);
            }
            
            model.addAttribute("newReview", new ReviewDTO());
            return "books/view";

        } catch (Exception e) {
            log.error("Error viewing book", e);
            model.addAttribute("error", "Error loading book: " + e.getMessage());
            return "redirect:/books";
        }
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public String deleteBook(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            bookWebService.deleteBook(id);
            redirectAttributes.addFlashAttribute("message", "Cartea a fost ștearsă cu succes!");
        } catch (Exception e) {
            log.error("Error deleting book", e);
            redirectAttributes.addFlashAttribute("error", "Eroare la ștergerea cărții: " + e.getMessage());
        }
        return "redirect:/books";
    }

    private BookPageDTO createEmptyPage() {
        BookPageDTO emptyPage = new BookPageDTO();
        emptyPage.setContent(java.util.Collections.emptyList());
        emptyPage.setTotalElements(0L);
        emptyPage.setTotalPages(0);
        emptyPage.setNumber(0);
        emptyPage.setSize(10);
        emptyPage.setEmpty(true);
        emptyPage.setFirst(true);
        emptyPage.setLast(true);
        return emptyPage;
    }
}