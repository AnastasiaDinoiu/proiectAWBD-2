package com.library.review.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BookDTO {
    private Long id;
    private String isbn;
    private String title;
    private String description;
    private LocalDate publicationDate;
    private Integer pages;
    private BigDecimal price;
    private Integer availableCopies;

    public BookDTO() {
    }

    public BookDTO(Long id, String isbn, String title, String description, LocalDate publicationDate, Integer pages, BigDecimal price, Integer availableCopies) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.description = description;
        this.publicationDate = publicationDate;
        this.pages = pages;
        this.price = price;
        this.availableCopies = availableCopies;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(Integer availableCopies) {
        this.availableCopies = availableCopies;
    }
}