package com.library.book.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

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

    private Long categoryId;
    private String categoryName;

    private Long publisherId;
    private String publisherName;

    private Set<Long> authorIds;
    private Set<String> authorNames;

    private Double averageRating;
    private Integer totalReviews;

    public BookDTO() {
    }

    public BookDTO(Long id, String isbn, String title, String description, LocalDate publicationDate, Integer pages, BigDecimal price, Integer availableCopies, Long categoryId, String categoryName, Long publisherId, String publisherName, Set<Long> authorIds, Set<String> authorNames, Double averageRating, Integer totalReviews) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.description = description;
        this.publicationDate = publicationDate;
        this.pages = pages;
        this.price = price;
        this.availableCopies = availableCopies;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.publisherId = publisherId;
        this.publisherName = publisherName;
        this.authorIds = authorIds;
        this.authorNames = authorNames;
        this.averageRating = averageRating;
        this.totalReviews = totalReviews;
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

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(Long publisherId) {
        this.publisherId = publisherId;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public Set<Long> getAuthorIds() {
        return authorIds;
    }

    public void setAuthorIds(Set<Long> authorIds) {
        this.authorIds = authorIds;
    }

    public Set<String> getAuthorNames() {
        return authorNames;
    }

    public void setAuthorNames(Set<String> authorNames) {
        this.authorNames = authorNames;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public Integer getTotalReviews() {
        return totalReviews;
    }

    public void setTotalReviews(Integer totalReviews) {
        this.totalReviews = totalReviews;
    }
}