package com.library.web.dto;

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

    private LocalDate loanDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private String status;
    private String notes;

    @NotNull(message = "Days to return is required")
    private Integer daysToReturn = 14;
}