package com.library.web.controller;

import com.library.web.dto.LoanDTO;
import com.library.web.service.LoanWebService;
import com.library.web.service.UserWebService;
import com.library.web.service.BookWebService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/loans")
@RequiredArgsConstructor
@Slf4j
public class LoanWebController {

    private final LoanWebService loanWebService;
    private final UserWebService userWebService;
    private final BookWebService bookWebService;

    @GetMapping
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public String listLoans(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "loanDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(required = false) String status,
            Model model) {

        log.info("Listing loans - page: {}, size: {}", page, size);

        try {
            var loanPage = loanWebService.getAllLoans(page, size, sortBy, sortDirection, status);

            model.addAttribute("loanPage", loanPage);
            model.addAttribute("currentPage", page);
            model.addAttribute("sortBy", sortBy);
            model.addAttribute("sortDirection", sortDirection);
            model.addAttribute("selectedStatus", status);

            return "loans/list";

        } catch (Exception e) {
            log.error("Error listing loans", e);
            model.addAttribute("error", "Error loading loans: " + e.getMessage());
            return "loans/list";
        }
    }

    @GetMapping("/my-loans")
    public String myLoans(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        log.info("Listing loans for user: {}", userDetails.getUsername());

        try {
            var user = userWebService.getUserByUsername(userDetails.getUsername());
            var loanPage = loanWebService.getLoansByUser(user.getId(), page, size);

            model.addAttribute("loanPage", loanPage);
            model.addAttribute("currentPage", page);

            return "loans/my-loans";

        } catch (Exception e) {
            log.error("Error listing user loans", e);
            model.addAttribute("error", "Error loading your loans: " + e.getMessage());
            return "loans/my-loans";
        }
    }

    @GetMapping("/create")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public String showCreateForm(Model model) {
        try {
            model.addAttribute("loan", new LoanDTO());
            model.addAttribute("books", bookWebService.getAllBooks());
            return "loans/create";
        } catch (Exception e) {
            log.error("Error loading create form", e);
            model.addAttribute("error", "Error loading form data");
            return "redirect:/loans";
        }
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public String createLoan(@ModelAttribute LoanDTO loanDTO,
                             RedirectAttributes redirectAttributes) {

        log.info("Creating new loan for user: {} and book: {}", loanDTO.getUserId(), loanDTO.getBookId());

        try {
            loanWebService.createLoan(loanDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Loan created successfully!");
            return "redirect:/loans";

        } catch (Exception e) {
            log.error("Error creating loan", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating loan: " + e.getMessage());
            return "redirect:/loans";
        }
    }

    @PostMapping("/{id}/return")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public String returnBook(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        log.info("Returning book for loan: {}", id);

        try {
            loanWebService.returnBook(id);
            redirectAttributes.addFlashAttribute("successMessage", "Book returned successfully!");
            return "redirect:/loans";

        } catch (Exception e) {
            log.error("Error returning book", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error returning book: " + e.getMessage());
            return "redirect:/loans";
        }
    }
}