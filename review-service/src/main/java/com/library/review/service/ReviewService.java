package com.library.review.service;

import com.library.review.client.BookServiceClient;
import com.library.review.client.UserServiceClient;
import com.library.review.dto.BookDTO;
import com.library.review.dto.UserDTO;
import com.library.review.entity.Review;
import com.library.review.exception.ResourceNotFoundException;
import com.library.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookServiceClient bookServiceClient;
    private final UserServiceClient userServiceClient;

    public Page<Review> findAll(Pageable pageable) {
        log.debug("Finding all reviews with pagination");
        return reviewRepository.findAll(pageable);
    }

    public Review findById(Long id) {
        log.debug("Finding review by id: {}", id);
        return reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + id));
    }

    public Review createReview(Long userId, Long bookId, Integer rating, String comment) {
        log.debug("Creating review - User: {}, Book: {}, Rating: {}", userId, bookId, rating);

        UserDTO user = userServiceClient.findById(userId);
        BookDTO book = bookServiceClient.findById(bookId);

        // Verifică dacă user-ul a mai făcut review pentru această carte
        if (reviewRepository.existsByBookIdAndUserId(bookId, userId)) {
            throw new IllegalStateException("User already reviewed this book");
        }

        Review review = new Review();
        review.setUserId(userId);
        review.setBookId(bookId);
        review.setRating(rating);
        review.setComment(comment);

        return reviewRepository.save(review);
    }

    public Review update(Long id, Review review) {
        log.debug("Updating review with id: {}", id);
        Review existingReview = findById(id);

        existingReview.setRating(review.getRating());
        existingReview.setComment(review.getComment());

        return reviewRepository.save(existingReview);
    }

    public void deleteById(Long id) {
        log.debug("Deleting review with id: {}", id);
        Review review = findById(id);
        reviewRepository.delete(review);
    }

    public Page<Review> findByBook(Long bookId, Pageable pageable) {
        log.debug("Finding reviews by book: {}", bookId);
        return reviewRepository.findByBookId(bookId, pageable);
    }

    public Page<Review> findByUser(Long userId, Pageable pageable) {
        log.debug("Finding reviews by user: {}", userId);
        return reviewRepository.findByUserId(userId, pageable);
    }

    public Double getAverageRatingForBook(Long bookId) {
        log.debug("Getting average rating for book: {}", bookId);
        Double average = reviewRepository.getAverageRatingByBookId(bookId);
        return average != null ? average : 0.0;
    }
}