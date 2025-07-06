package com.library.book.controller;

import com.library.book.dto.BookAvailabilityDTO;
import com.library.book.service.BookAvailabilityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class BookAvailabilityController {

    private static final Logger log = LoggerFactory.getLogger(BookAvailabilityController.class);
    private final BookAvailabilityService bookAvailabilityService;

    public BookAvailabilityController(BookAvailabilityService bookAvailabilityService) {
        this.bookAvailabilityService = bookAvailabilityService;
    }

    @GetMapping("/availability")
    public Page<BookAvailabilityDTO> getBookAvailability(Pageable pageable) {
        return bookAvailabilityService.getBookAvailability(pageable);
    }

    @GetMapping("/{id}/availability")
    public BookAvailabilityDTO getBookAvailabilityById(@PathVariable Long id) {
        return bookAvailabilityService.getBookAvailabilityById(id);
    }
}