package com.library.loan.controller;

import com.library.loan.dto.LoanDTO;
import com.library.loan.dto.UserDTO;
import com.library.loan.entity.Loan;
import com.library.loan.service.LoanService;
import com.library.loan.client.UserServiceClient;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
@Slf4j
public class LoanController {

    private final LoanService loanService;
    private final UserServiceClient userServiceClient;

    @GetMapping
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public Page<Loan> listLoans(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "loanDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(required = false) String status) {

        log.info("Listing loans - page: {}, size: {}", page, size);

        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC")
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(direction, sortBy));

        if (status != null && !status.isEmpty()) {
            return loanService.findByStatus(Loan.LoanStatus.valueOf(status), pageRequest);
        } else {
            return loanService.findAll(pageRequest);
        }
    }

    @GetMapping("/my-loans")
    public Page<Loan> myLoans(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("Listing loans for user: {}", userDetails.getUsername());

        UserDTO user = userServiceClient.findByUsername(userDetails.getUsername());
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "loanDate"));

        return loanService.findByUser(user.getId(), pageRequest);
    }

//    @GetMapping("/new")
//    @PreAuthorize("hasRole('LIBRARIAN')")
//    public String showCreateForm(Model model) {
//        log.info("Showing loan creation form");
//
//        List<User> users = userServiceClient.findAll(PageRequest.of(0, 100)).getContent();
//        List<Book> availableBooks = bookServiceClient.findAvailableBooks();
//
//        model.addAttribute("loanDTO", new LoanDTO());
//        model.addAttribute("users", users);
//        model.addAttribute("books", availableBooks);
//
//        return "loans/form";
//    }

    @PostMapping("/new")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<Loan> createLoan(@Valid @ModelAttribute LoanDTO loanDTO) {
        log.info("Creating new loan for user: {} and book: {}", loanDTO.getUserId(), loanDTO.getBookId());

        try {
            Loan loan = loanService.createLoan(
                    loanDTO.getUserId(),
                    loanDTO.getBookId(),
                    loanDTO.getDaysToReturn()
            );
            return ResponseEntity.ok(loan);
        } catch (Exception e) {
            log.error("Error creating loan", e);
            throw e;
        }
    }

    @PostMapping("/{id}/return")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<Void> returnBook(@PathVariable Long id) {
        log.info("Returning book for loan: {}", id);

        try {
            loanService.returnBook(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error returning book", e);
            throw e;
        }
    }

    @PostMapping("/update-overdue")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<Void> updateOverdueLoans() {
        log.info("Updating overdue loans");

        try {
            loanService.updateOverdueLoans();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error updating overdue loans", e);
            throw e;
        }
    }

    @GetMapping("/book/{bookId}/active-count")
    public Integer getActiveLoanCountByBookId(@PathVariable Long bookId) {
        return loanService.getActiveLoanCountByBookId(bookId);
    }
}