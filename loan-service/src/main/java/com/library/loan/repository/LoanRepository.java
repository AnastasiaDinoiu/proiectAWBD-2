package com.library.loan.repository;

import com.library.loan.entity.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    Page<Loan> findByUserId(Long userId, Pageable pageable);

    Page<Loan> findByBookId(Long bookId, Pageable pageable);

    Page<Loan> findByStatus(Loan.LoanStatus status, Pageable pageable);

    @Query("SELECT l FROM Loan l WHERE l.dueDate < :date AND l.status = 'ACTIVE'")
    List<Loan> findOverdueLoans(LocalDate date);

    boolean existsByBookIdAndUserIdAndStatus(Long bookId, Long userId, Loan.LoanStatus status);

    Integer countByBookIdAndStatusIn(Long bookId, List<Loan.LoanStatus> statuses);
}