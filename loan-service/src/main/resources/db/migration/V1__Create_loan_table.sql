-- Loans table - fără foreign keys externe
CREATE TABLE IF NOT EXISTS loans (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     user_id BIGINT NOT NULL,  -- Doar ID-ul, fără foreign key
                                     book_id BIGINT NOT NULL,  -- Doar ID-ul, fără foreign key
                                     loan_date DATE NOT NULL,
                                     due_date DATE NOT NULL,
                                     return_date DATE,
                                     status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    );

-- Index pentru performanță
CREATE INDEX idx_loans_user_id ON loans(user_id);
CREATE INDEX idx_loans_book_id ON loans(book_id);
CREATE INDEX idx_loans_status ON loans(status);