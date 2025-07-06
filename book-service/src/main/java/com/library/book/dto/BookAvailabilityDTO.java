package com.library.book.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

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

    public BookAvailabilityDTO() {
    }

    public BookAvailabilityDTO(Long bookId, String title, String authors, String isbn, LocalDate publicationDate, Integer totalCopies, Integer loanedCopies, Integer availableCopies, String availabilityStatus, String categoryName, String publisherName, BigDecimal price) {
        this.bookId = bookId;
        this.title = title;
        this.authors = authors;
        this.isbn = isbn;
        this.publicationDate = publicationDate;
        this.totalCopies = totalCopies;
        this.loanedCopies = loanedCopies;
        this.availableCopies = availableCopies;
        this.availabilityStatus = availabilityStatus;
        this.categoryName = categoryName;
        this.publisherName = publisherName;
        this.price = price;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public Integer getTotalCopies() {
        return totalCopies;
    }

    public void setTotalCopies(Integer totalCopies) {
        this.totalCopies = totalCopies;
    }

    public Integer getLoanedCopies() {
        return loanedCopies;
    }

    public void setLoanedCopies(Integer loanedCopies) {
        this.loanedCopies = loanedCopies;
    }

    public Integer getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(Integer availableCopies) {
        this.availableCopies = availableCopies;
    }

    public String getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(String availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}