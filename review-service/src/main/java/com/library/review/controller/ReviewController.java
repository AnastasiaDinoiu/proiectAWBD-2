package com.library.review.controller;

import com.library.review.dto.ReviewDTO;
import com.library.review.entity.Review;
import com.library.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private static final Logger log = LoggerFactory.getLogger(ReviewController.class);
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public Page<Review> getAllReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "reviewDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC")
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(direction, sortBy));

        return reviewService.findAll(pageRequest);
    }

    @GetMapping("/book/{bookId}")
    public Page<Review> getReviewsByBook(@PathVariable Long bookId,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "reviewDate"));
        return reviewService.findByBook(bookId, pageRequest);
    }

    @GetMapping("/user/{userId}")
    public Page<Review> getReviewsByUser(@PathVariable Long userId,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "reviewDate"));
        return reviewService.findByUser(userId, pageRequest);
    }

    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody ReviewDTO reviewDTO) {
        log.info("Creating review for book: {} by user: {}", reviewDTO.getBookId(), reviewDTO.getUserId());

        Review review = reviewService.createReview(
                reviewDTO.getUserId(),
                reviewDTO.getBookId(),
                reviewDTO.getRating(),
                reviewDTO.getComment()
        );
        return ResponseEntity.ok(review);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        log.info("Deleting review: {}", id);
        reviewService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/book/{bookId}/average-rating")
    public Double getAverageRating(@PathVariable Long bookId) {
        return reviewService.getAverageRatingForBook(bookId);
    }
}