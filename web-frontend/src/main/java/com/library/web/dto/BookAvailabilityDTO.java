package com.library.web.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BookAvailabilityDTO {
    private Long bookId;
    private String title;
    private String authors;
    private String isbn;
    private LocalDate publicationDate;
    private Long totalCopies;
    private Long loanedCopies;
    private Long availableCopies;
    private String availabilityStatus;
    private String categoryName;
    private String publisherName;
}