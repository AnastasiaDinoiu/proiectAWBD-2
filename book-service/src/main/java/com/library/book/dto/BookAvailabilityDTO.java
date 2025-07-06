package com.library.book.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class BookAvailabilityDTO {
    private Long bookId;
    private String title;
    private String authors;
    private String isbn;
    private LocalDate publicationDate;
    private Integer totalCopies;
    private Integer loanedCopies;
    private Integer availableCopies;
    private String availabilityStatus;
    private String categoryName;
    private String publisherName;
    private BigDecimal price;
}