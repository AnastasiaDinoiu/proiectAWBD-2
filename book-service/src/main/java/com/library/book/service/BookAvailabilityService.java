package com.library.book.service;

import com.library.book.client.LoanServiceClient;
import com.library.book.dto.BookAvailabilityDTO;
import com.library.book.entity.Book;
import com.library.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookAvailabilityService {

    private final BookRepository bookRepository;
    private final LoanServiceClient loanServiceClient;

    public Page<BookAvailabilityDTO> getBookAvailability(Pageable pageable) {
        Page<Book> books = bookRepository.findAll(pageable);

        return books.map(this::convertToAvailabilityDTO);
    }

    public BookAvailabilityDTO getBookAvailabilityById(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        return convertToAvailabilityDTO(book);
    }

    private BookAvailabilityDTO convertToAvailabilityDTO(Book book) {
        BookAvailabilityDTO dto = new BookAvailabilityDTO();
        dto.setBookId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setIsbn(book.getIsbn());
        dto.setPublicationDate(book.getPublicationDate());
        dto.setTotalCopies(book.getAvailableCopies());

        // Calculează autorii
        if (book.getAuthors() != null && !book.getAuthors().isEmpty()) {
            String authors = book.getAuthors().stream()
                    .map(author -> author.getFirstName() + " " + author.getLastName())
                    .collect(Collectors.joining(", "));
            dto.setAuthors(authors);
        } else {
            dto.setAuthors("Unknown Author");
        }

        // Obține numărul de împrumuturi active
        Integer loanedCopies = 0;
        try {
            loanedCopies = loanServiceClient.getActiveLoanCountByBookId(book.getId());
        } catch (Exception e) {
            log.error("Error fetching loan count for book {}: {}", book.getId(), e.getMessage());
            // Fallback la 0 dacă serviciul nu este disponibil
        }

        dto.setLoanedCopies(loanedCopies);
        dto.setAvailableCopies(book.getAvailableCopies() - loanedCopies);

        // Calculează statusul disponibilității
        if (dto.getAvailableCopies() > 0) {
            dto.setAvailabilityStatus("Available");
        } else {
            dto.setAvailabilityStatus("Not Available");
        }

        // Adaugă informații despre categorie și editor
        if (book.getCategory() != null) {
            dto.setCategoryName(book.getCategory().getName());
        }
        if (book.getPublisher() != null) {
            dto.setPublisherName(book.getPublisher().getName());
        }

        return dto;
    }
}