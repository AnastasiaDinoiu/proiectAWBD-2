package com.library.loan.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Data
public class BookDTO {

    private Long id;

    @NotBlank(message = "ISBN is required")
    @Pattern(regexp = "^(?=(?:[^0-9]*[0-9]){10}(?:(?:[^0-9]*[0-9]){3})?$)[\\d-]+$",
            message = "Invalid ISBN format")
    private String isbn;

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title cannot exceed 255 characters")
    private String title;

    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    private String description;

    @NotNull(message = "Publication date is required")
    @PastOrPresent(message = "Publication date cannot be in the future")
    private LocalDate publicationDate;

    @Min(value = 1, message = "Book must have at least 1 page")
    @Max(value = 10000, message = "Page count seems unrealistic")
    private Integer pages;

    @DecimalMin(value = "0.0", message = "Price cannot be negative")
    @DecimalMax(value = "9999.99", message = "Price cannot exceed 9999.99")
    private BigDecimal price;

    @Min(value = 0, message = "Available copies cannot be negative")
    private Integer availableCopies = 0;
}