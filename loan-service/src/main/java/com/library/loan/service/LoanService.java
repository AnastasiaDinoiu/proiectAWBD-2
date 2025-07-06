package com.library.loan.service;

import com.library.loan.client.BookServiceClient;
import com.library.loan.client.UserServiceClient;
import com.library.loan.dto.BookDTO;
import com.library.loan.dto.UserDTO;
import com.library.loan.entity.Loan;
import com.library.loan.exception.ResourceNotFoundException;
import com.library.loan.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookServiceClient bookServiceClient;
    private final UserServiceClient userServiceClient;

    public Page<Loan> findAll(Pageable pageable) {
        log.debug("Finding all loans with pagination");
        return loanRepository.findAll(pageable);
    }

    public Loan findById(Long id) {
        log.debug("Finding loan by id: {}", id);
        return loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found with id: " + id));
    }

    @Transactional
    public Loan createLoan(Long userId, Long bookId, Integer daysToReturn) {
        log.debug("Creating loan for user: {} and book: {}", userId, bookId);

        UserDTO user = userServiceClient.findById(userId);
        BookDTO book = bookServiceClient.findById(bookId);

        // Check if user already has an active loan for this book
        if (loanRepository.existsByBookIdAndUserIdAndStatus(bookId, userId, Loan.LoanStatus.ACTIVE)) {
            throw new IllegalStateException("User already has an active loan for this book");
        }

        // Check book availability
        if (book.getAvailableCopies() <= 0) {
            throw new IllegalStateException("Book is not available for loan");
        }

        Loan loan = new Loan();
        loan.setUserId(userId);
        loan.setBookId(bookId);
        loan.setLoanDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusDays(daysToReturn));
        loan.setStatus(Loan.LoanStatus.ACTIVE);

        // Decrement available copies
        bookServiceClient.decrementAvailableCopies(bookId);

        return loanRepository.save(loan);
    }

    @Transactional
    public Loan returnBook(Long loanId) {
        log.debug("Returning book for loan: {}", loanId);

        Loan loan = findById(loanId);

        if (loan.getStatus() != Loan.LoanStatus.ACTIVE) {
            throw new IllegalStateException("Loan is not active");
        }

        loan.setReturnDate(LocalDate.now());
        loan.setStatus(Loan.LoanStatus.RETURNED);

        // Increment available copies
        bookServiceClient.incrementAvailableCopies(loan.getBookId());

        return loanRepository.save(loan);
    }

    public Page<Loan> findByUser(Long userId, Pageable pageable) {
        log.debug("Finding loans by user: {}", userId);
        return loanRepository.findByUserId(userId, pageable);
    }

    public Page<Loan> findByStatus(Loan.LoanStatus status, Pageable pageable) {
        log.debug("Finding loans by status: {}", status);
        return loanRepository.findByStatus(status, pageable);
    }

    public Page<Loan> findByBook(Long bookId, Pageable pageable) {
        log.debug("Finding loans by book: {}", bookId);
        return loanRepository.findByBookId(bookId, pageable);
    }

    @Transactional
    public void updateOverdueLoans() {
        log.debug("Updating overdue loans");
        loanRepository.findOverdueLoans(LocalDate.now()).forEach(loan -> {
            loan.setStatus(Loan.LoanStatus.OVERDUE);
            loanRepository.save(loan);
        });
    }

    public Integer getActiveLoanCountByBookId(Long bookId) {
        return loanRepository.countByBookIdAndStatusIn(bookId,
                List.of(Loan.LoanStatus.ACTIVE, Loan.LoanStatus.OVERDUE));
    }
}