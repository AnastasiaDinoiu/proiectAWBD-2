package com.library.book.service;

import com.library.book.entity.Book;
import com.library.book.exception.ResourceNotFoundException;
import com.library.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BookService {

    private final BookRepository bookRepository;

    public Page<Book> findAll(Pageable pageable) {
        log.debug("Finding all books with pagination: {}", pageable);
        return bookRepository.findAll(pageable);
    }

    public Book findById(Long id) {
        log.debug("Finding book by id: {}", id);
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
    }

    public Book save(Book book) {
        log.debug("Saving book: {}", book.getTitle());
        return bookRepository.save(book);
    }

    public Book update(Long id, Book book) {
        log.debug("Updating book with id: {}", id);
        Book existingBook = findById(id);

        existingBook.setTitle(book.getTitle());
        existingBook.setIsbn(book.getIsbn());
        existingBook.setDescription(book.getDescription());
        existingBook.setPublicationDate(book.getPublicationDate());
        existingBook.setPages(book.getPages());
        existingBook.setPrice(book.getPrice());
        existingBook.setAvailableCopies(book.getAvailableCopies());
        existingBook.setCategory(book.getCategory());
        existingBook.setPublisher(book.getPublisher());
        existingBook.setAuthors(book.getAuthors());

        return bookRepository.save(existingBook);
    }

    public void deleteById(Long id) {
        log.debug("Deleting book with id: {}", id);
        Book book = findById(id);
        bookRepository.delete(book);
    }

    public Page<Book> searchByTitle(String title, Pageable pageable) {
        log.debug("Searching books by title: {}", title);
        return bookRepository.findByTitleContainingIgnoreCase(title, pageable);
    }

    public Page<Book> findByCategory(Long categoryId, Pageable pageable) {
        log.debug("Finding books by category: {}", categoryId);
        return bookRepository.findByCategoryId(categoryId, pageable);
    }

    public List<Book> findAvailableBooks() {
        log.debug("Finding available books");
        return bookRepository.findByAvailableCopiesGreaterThan(0);
    }

    public boolean isBookAvailable(Long bookId) {
        Book book = findById(bookId);
        return book.getAvailableCopies() > 0;
    }

    @Transactional
    public void decrementAvailableCopies(Long bookId) {
        Book book = findById(bookId);
        if (book.getAvailableCopies() > 0) {
            book.setAvailableCopies(book.getAvailableCopies() - 1);
            bookRepository.save(book);
        } else {
            throw new IllegalStateException("No available copies for book: " + book.getTitle());
        }
    }

    @Transactional
    public void incrementAvailableCopies(Long bookId) {
        Book book = findById(bookId);
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);
    }
}