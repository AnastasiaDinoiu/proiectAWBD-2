package com.library.web.controller;

import com.library.web.dto.ReviewDTO;
import com.library.web.service.ReviewWebService;
import com.library.web.service.UserWebService;
import com.library.web.service.BookWebService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/reviews")
@RequiredArgsConstructor
@Slf4j
public class ReviewWebController {

    private final ReviewWebService reviewWebService;
    private final UserWebService userWebService;
    private final BookWebService bookWebService;

    @GetMapping
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public String listReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "reviewDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection,
            Model model) {

        log.info("Listing reviews - page: {}, size: {}", page, size);

        try {
            var reviewPage = reviewWebService.getAllReviews(page, size, sortBy, sortDirection);

            model.addAttribute("reviewPage", reviewPage);
            model.addAttribute("currentPage", page);
            model.addAttribute("sortBy", sortBy);
            model.addAttribute("sortDirection", sortDirection);

            return "reviews/list";

        } catch (Exception e) {
            log.error("Error listing reviews", e);
            model.addAttribute("error", "Error loading reviews: " + e.getMessage());
            return "reviews/list";
        }
    }

    @GetMapping("/book/{bookId}")
    public String getReviewsByBook(@PathVariable Long bookId,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size,
                                   Model model) {

        log.info("Listing reviews for book: {}", bookId);

        try {
            var reviewPage = reviewWebService.getReviewsByBook(bookId, page, size);
            var book = bookWebService.getBookById(bookId);

            model.addAttribute("reviewPage", reviewPage);
            model.addAttribute("book", book);
            model.addAttribute("currentPage", page);

            return "reviews/by-book";

        } catch (Exception e) {
            log.error("Error listing reviews for book", e);
            model.addAttribute("error", "Error loading reviews: " + e.getMessage());
            return "reviews/by-book";
        }
    }

    @GetMapping("/my-reviews")
    public String myReviews(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        log.info("Listing reviews for user: {}", userDetails.getUsername());

        try {
            var user = userWebService.getUserByUsername(userDetails.getUsername());
            if (user == null) {
                model.addAttribute("error", "User not found: " + userDetails.getUsername());
                return "reviews/my-reviews";
            }
            
            var reviewPage = reviewWebService.getReviewsByUser(user.getId(), page, size);

            model.addAttribute("reviewPage", reviewPage);
            model.addAttribute("currentPage", page);

            return "reviews/my-reviews";

        } catch (Exception e) {
            log.error("Error listing user reviews", e);
            model.addAttribute("error", "Error loading your reviews: " + e.getMessage());
            return "reviews/my-reviews";
        }
    }

    @PostMapping("/create")
    public String createReview(@Valid @ModelAttribute ReviewDTO reviewDTO,
                               BindingResult bindingResult,
                               @AuthenticationPrincipal UserDetails userDetails,
                               RedirectAttributes redirectAttributes) {

        log.info("Creating review for book: {} by user: {}", reviewDTO.getBookId(), userDetails.getUsername());

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please check your input and try again.");
            return "redirect:/books/" + reviewDTO.getBookId();
        }

        try {
            // Set user ID from authenticated user
            var user = userWebService.getUserByUsername(userDetails.getUsername());
            if (user == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "User not found: " + userDetails.getUsername());
                return "redirect:/books/" + reviewDTO.getBookId();
            }
            
            reviewDTO.setUserId(user.getId());

            reviewWebService.createReview(reviewDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Review added successfully!");

            return "redirect:/books/" + reviewDTO.getBookId();

        } catch (Exception e) {
            log.error("Error creating review", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating review: " + e.getMessage());
            return "redirect:/books/" + reviewDTO.getBookId();
        }
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public String deleteReview(@PathVariable Long id,
                               @RequestParam(required = false) Long bookId,
                               RedirectAttributes redirectAttributes) {

        log.info("Deleting review: {}", id);

        try {
            reviewWebService.deleteReview(id);
            redirectAttributes.addFlashAttribute("successMessage", "Review deleted successfully!");

            if (bookId != null) {
                return "redirect:/books/" + bookId;
            } else {
                return "redirect:/reviews";
            }

        } catch (Exception e) {
            log.error("Error deleting review", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting review: " + e.getMessage());
            return "redirect:/reviews";
        }
    }
}