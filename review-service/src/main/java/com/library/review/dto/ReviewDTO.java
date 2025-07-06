package com.library.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewDTO {
    private Long id;

    @NotNull(message = "User is required")
    private Long userId;
    private String username;

    @NotNull(message = "Book is required")
    private Long bookId;
    private String bookTitle;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating cannot exceed 5")
    private Integer rating;

    @NotBlank(message = "Comment is required")
    private String comment;

    private LocalDateTime reviewDate;
    private LocalDateTime updatedAt;
}