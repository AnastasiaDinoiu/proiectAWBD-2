-- Reviews table - fără foreign keys externe
CREATE TABLE IF NOT EXISTS reviews (
                                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       user_id BIGINT NOT NULL,  -- Doar ID-ul, fără foreign key
                                       book_id BIGINT NOT NULL,  -- Doar ID-ul, fără foreign key
                                       rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment VARCHAR(1000) NOT NULL,
    review_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    );

-- Index pentru performanță și constrainte business
CREATE INDEX idx_reviews_user_id ON reviews(user_id);
CREATE INDEX idx_reviews_book_id ON reviews(book_id);
CREATE UNIQUE INDEX idx_reviews_user_book ON reviews(user_id, book_id); -- Un user poate da un singur review per carte