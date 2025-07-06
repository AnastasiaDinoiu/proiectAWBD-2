package com.library.web.controller;

import com.library.web.dto.LoanDTO;
import com.library.web.dto.LoanPageDTO;
import com.library.web.dto.BookPageDTO;
import com.library.web.dto.UserPageDTO;
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

import java.util.Collections;

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
            LoanPageDTO loanPage = loanWebService.getAllLoans(page, size, sortBy, sortDirection, status);
            
            if (loanPage == null) {
                log.warn("LoanWebService returned null, creating empty page");
                loanPage = createEmptyLoanPage();
            }

            model.addAttribute("loanPage", loanPage);
            model.addAttribute("currentPage", page);
            model.addAttribute("pageSize", size);
            model.addAttribute("sortBy", sortBy);
            model.addAttribute("sortDirection", sortDirection);
            model.addAttribute("status", status);
            
            log.info("Successfully loaded {} loans", 
                    loanPage.getContent() != null ? loanPage.getContent().size() : 0);

            return "loans/list";

        } catch (Exception e) {
            log.error("Error listing loans", e);
            model.addAttribute("loanPage", createEmptyLoanPage());
            model.addAttribute("error", "Nu s-au putut încărca împrumuturile. Verifică dacă serviciul de împrumuturi rulează.");
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
            if (user == null) {
                model.addAttribute("error", "User not found: " + userDetails.getUsername());
                return "loans/my-loans";
            }
            
            var loanPage = loanWebService.getLoansByUser(user.getId(), page, size);

            model.addAttribute("loanPage", loanPage);
            model.addAttribute("currentPage", page);

            return "loans/my-loans";

        } catch (Exception e) {
            log.error("Error listing user loans", e);
            model.addAttribute("loanPage", createEmptyLoanPage());
            model.addAttribute("error", "Failed to load your loans");
            return "loans/my-loans";
        }
    }

    @GetMapping("/new")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public String showCreateForm(Model model) {
        log.info("Showing create loan form");
        
        try {
            // Încarcă datele necesare pentru dropdown-uri
            BookPageDTO bookPage = bookWebService.getAllBooks(0, 50, null);
            UserPageDTO userPage = userWebService.getAllUsers(0, 50);
            
            // Creează un loan nou pentru form
            LoanDTO loanDTO = new LoanDTO();
            
            model.addAttribute("loanDTO", loanDTO);
            model.addAttribute("books", bookPage != null ? bookPage.getContent() : Collections.emptyList());
            model.addAttribute("users", userPage != null ? userPage.getContent() : Collections.emptyList());
            
            return "loans/form";
            
        } catch (Exception e) {
            log.error("Error preparing loan form: {}", e.getMessage(), e);
            model.addAttribute("loanDTO", new LoanDTO());
            model.addAttribute("books", Collections.emptyList());
            model.addAttribute("users", Collections.emptyList());
            model.addAttribute("error", "Failed to load form data: " + e.getMessage());
            return "loans/form";
        }
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public String showEditForm(@PathVariable Long id, Model model) {
        log.info("Showing edit form for loan id: {}", id);
        
        try {
            LoanDTO loan = loanWebService.getLoan(id);
            
            if (loan == null) {
                model.addAttribute("error", "Loan not found");
                return "redirect:/loans";
            }
            
            // Încarcă datele necesare pentru dropdown-uri
            BookPageDTO bookPage = bookWebService.getAllBooks(0, 50, null);
            UserPageDTO userPage = userWebService.getAllUsers(0, 50);
            
            model.addAttribute("loanDTO", loan);
            model.addAttribute("books", bookPage != null ? bookPage.getContent() : Collections.emptyList());
            model.addAttribute("users", userPage != null ? userPage.getContent() : Collections.emptyList());
            model.addAttribute("isEdit", true);
            
            return "loans/form";
            
        } catch (Exception e) {
            log.error("Error loading loan for edit {}: {}", id, e.getMessage(), e);
            model.addAttribute("error", "Failed to load loan: " + e.getMessage());
            return "redirect:/loans";
        }
    }

    @PostMapping("/new")
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

    @PostMapping("/update-overdue")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public String updateOverdueLoans(RedirectAttributes redirectAttributes) {

        log.info("Updating overdue loans");

        try {
            loanWebService.updateOverdueLoans();
            redirectAttributes.addFlashAttribute("successMessage", "Overdue loans updated successfully!");
            return "redirect:/loans";

        } catch (Exception e) {
            log.error("Error updating overdue loans", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating overdue loans: " + e.getMessage());
            return "redirect:/loans";
        }
    }

    private LoanPageDTO createEmptyLoanPage() {
        LoanPageDTO emptyPage = new LoanPageDTO();
        emptyPage.setContent(Collections.emptyList());
        emptyPage.setTotalElements(0);
        emptyPage.setTotalPages(0);
        emptyPage.setNumber(0);
        emptyPage.setSize(10);
        emptyPage.setFirst(true);
        emptyPage.setLast(true);
        emptyPage.setEmpty(true);
        return emptyPage;
    }
}