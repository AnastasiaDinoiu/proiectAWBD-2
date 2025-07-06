package com.library.web.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
    private Long id;
    private String isbn;
    private String title;
    private String description;
    private LocalDate publicationDate;
    private int pages;
    private int availableCopies;
    private BigDecimal price;
    private CategoryDTO category;
    private PublisherDTO publisher;
    private List<AuthorDTO> authors;
}