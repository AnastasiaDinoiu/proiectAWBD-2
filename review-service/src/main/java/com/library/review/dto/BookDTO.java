package com.library.review.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class BookDTO {
    private Long id;
    private String isbn;
    private String title;
    private String description;
    private LocalDate publicationDate;
    private Integer pages;
    private BigDecimal price;
    private Integer availableCopies;
}