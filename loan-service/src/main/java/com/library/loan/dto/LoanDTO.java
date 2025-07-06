package com.library.loan.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class LoanDTO {

    private Long id;

    @NotNull(message = "User is required")
    private Long userId;
    private String username;

    @NotNull(message = "Book is required")
    private Long bookId;
    private String bookTitle;

    @NotNull(message = "Loan duration is required")
    @Min(value = 1, message = "Loan must be for at least 1 day")
    @Max(value = 30, message = "Loan cannot exceed 30 days")
    private Integer daysToReturn = 14;

    private LocalDate loanDate;
    private LocalDate dueDate;
    private LocalDate returnDate;

    private String status;
    private String notes;

    // Calculated fields
    private boolean isOverdue;
    private Integer daysOverdue;
}