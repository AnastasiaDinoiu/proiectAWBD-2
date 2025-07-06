-- Users table
CREATE TABLE IF NOT EXISTS users (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    );

-- User details table
CREATE TABLE IF NOT EXISTS user_details (
                                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                            user_id BIGINT UNIQUE NOT NULL,
                                            first_name VARCHAR(50),
    last_name VARCHAR(50),
    phone_number VARCHAR(20),
    address VARCHAR(255),
    date_of_birth DATE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
    );