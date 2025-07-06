package com.library.book.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "books")
@Data
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "ISBN is required")
    @Column(unique = true, nullable = false)
    private String isbn;

    @NotBlank(message = "Title is required")
    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    @NotNull(message = "Publication date is required")
    private LocalDate publicationDate;

    @Min(value = 1, message = "Pages must be at least 1")
    private Integer pages;

    @DecimalMin(value = "0.0", message = "Price must be positive")
    private BigDecimal price;

    @Min(value = 0, message = "Available copies cannot be negative")
    private Integer availableCopies;

    @ManyToMany
    @JoinTable(
            name = "book_authors",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Author> authors = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "category_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Category category;

    @ManyToOne
    @JoinColumn(name = "publisher_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Publisher publisher;
}