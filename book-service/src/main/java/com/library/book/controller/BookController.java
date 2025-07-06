package com.library.book.controller;

import com.library.book.dto.BookDTO;
import com.library.book.entity.Book;
import com.library.book.service.BookService;
import com.library.book.service.CategoryService;
import com.library.book.service.PublisherService;
import com.library.book.service.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private static final Logger log = LoggerFactory.getLogger(BookController.class);
    private final BookService bookService;
    private final CategoryService categoryService;
    private final PublisherService publisherService;
    private final AuthorService authorService;

    @GetMapping
    public Page<BookDTO> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection,
            @RequestParam(required = false) String search) {

        log.info("Getting books - page: {}, size: {}, sortBy: {}, sortDirection: {}, search: {}",
                page, size, sortBy, sortDirection, search);

        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC")
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<Book> bookPage;
        if (search != null && !search.isEmpty()) {
            bookPage = bookService.searchByTitle(search.trim(), pageRequest);
        } else {
            bookPage = bookService.findAll(pageRequest);
        }

        return bookPage.map(this::convertToDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        log.info("Getting book with id: {}", id);

        try {
            Book book = bookService.findById(id);
            return ResponseEntity.ok(convertToDTO(book));
        } catch (Exception e) {
            log.error("Book not found with id: {}", id, e);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<BookDTO> createBook(@Valid @RequestBody BookDTO bookDTO) {
        log.info("Creating new book: {}", bookDTO.getTitle());

        try {
            Book book = convertToEntity(bookDTO);
            Book savedBook = bookService.save(book);
            return ResponseEntity.ok(convertToDTO(savedBook));
        } catch (Exception e) {
            log.error("Error creating book", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @Valid @RequestBody BookDTO bookDTO) {
        log.info("Updating book: {}", id);

        try {
            Book book = convertToEntity(bookDTO);
            Book updatedBook = bookService.update(id, book);
            return ResponseEntity.ok(convertToDTO(updatedBook));
        } catch (Exception e) {
            log.error("Error updating book", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        log.info("Deleting book: {}", id);

        try {
            bookService.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error deleting book", e);
            return ResponseEntity.badRequest().build();
        }
    }

    private BookDTO convertToDTO(Book book) {
        BookDTO dto = new BookDTO();
        dto.setId(book.getId());
        dto.setIsbn(book.getIsbn());
        dto.setTitle(book.getTitle());
        dto.setDescription(book.getDescription());
        dto.setPublicationDate(book.getPublicationDate());
        dto.setPages(book.getPages());
        dto.setPrice(book.getPrice());
        dto.setAvailableCopies(book.getAvailableCopies());
        
        if (book.getCategory() != null) {
            dto.setCategoryId(book.getCategory().getId());
            dto.setCategoryName(book.getCategory().getName());
        }
        
        if (book.getPublisher() != null) {
            dto.setPublisherId(book.getPublisher().getId());
            dto.setPublisherName(book.getPublisher().getName());
        }
        
        return dto;
    }

    private Book convertToEntity(BookDTO dto) {
        Book book = new Book();
        book.setId(dto.getId());
        book.setIsbn(dto.getIsbn());
        book.setTitle(dto.getTitle());
        book.setDescription(dto.getDescription());
        book.setPublicationDate(dto.getPublicationDate());
        book.setPages(dto.getPages());
        book.setPrice(dto.getPrice());
        book.setAvailableCopies(dto.getAvailableCopies());
        
        if (dto.getCategoryId() != null) {
            book.setCategory(categoryService.findById(dto.getCategoryId()));
        }
        
        if (dto.getPublisherId() != null) {
            book.setPublisher(publisherService.findById(dto.getPublisherId()));
        }
        
        return book;
    }
}