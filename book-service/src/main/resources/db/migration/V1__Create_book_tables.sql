-- Categories table
CREATE TABLE IF NOT EXISTS categories (
                                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                          name VARCHAR(100) UNIQUE NOT NULL,
    description TEXT
    );

-- Publishers table
CREATE TABLE IF NOT EXISTS publishers (
                                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                          name VARCHAR(100) UNIQUE NOT NULL,
    address VARCHAR(255),
    phone_number VARCHAR(20),
    email VARCHAR(100),
    website VARCHAR(255)
    );

-- Authors table
CREATE TABLE IF NOT EXISTS authors (
                                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    biography TEXT,
    birth_date DATE,
    nationality VARCHAR(50)
    );

-- Books table (doar referințe interne)
CREATE TABLE IF NOT EXISTS books (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     isbn VARCHAR(20) UNIQUE NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    publication_date DATE NOT NULL,
    pages INT CHECK (pages >= 1),
    price DECIMAL(10, 2) CHECK (price >= 0),
    available_copies INT DEFAULT 0 CHECK (available_copies >= 0),
    category_id BIGINT,
    publisher_id BIGINT,
    FOREIGN KEY (category_id) REFERENCES categories(id),
    FOREIGN KEY (publisher_id) REFERENCES publishers(id)
    );

-- Book_Authors table (many-to-many între books și authors)
CREATE TABLE IF NOT EXISTS book_authors (
                                            book_id BIGINT NOT NULL,
                                            author_id BIGINT NOT NULL,
                                            PRIMARY KEY (book_id, author_id),
    FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE,
    FOREIGN KEY (author_id) REFERENCES authors(id) ON DELETE CASCADE
    );