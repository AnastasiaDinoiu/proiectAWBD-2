package com.library.book.controller;

import com.library.book.dto.BookAvailabilityDTO;
import com.library.book.service.BookAvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookAvailabilityController {

    private final BookAvailabilityService bookAvailabilityService;

    @GetMapping("/availability")
    public Page<BookAvailabilityDTO> getBookAvailability(Pageable pageable) {
        return bookAvailabilityService.getBookAvailability(pageable);
    }

    @GetMapping("/{id}/availability")
    public BookAvailabilityDTO getBookAvailabilityById(@PathVariable Long id) {
        return bookAvailabilityService.getBookAvailabilityById(id);
    }
}