package com.library.review.repository;

import com.library.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findByBookId(Long bookId, Pageable pageable);

    Page<Review> findByUserId(Long userId, Pageable pageable);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.bookId = :bookId")
    Double getAverageRatingByBookId(Long bookId);

    boolean existsByBookIdAndUserId(Long bookId, Long userId);
}