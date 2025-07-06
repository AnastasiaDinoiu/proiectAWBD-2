package com.library.loan.client;

import com.library.loan.dto.BookDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "book-service")
public interface BookServiceClient {

    @GetMapping("/api/books/{id}")
    BookDTO findById(@PathVariable Long id);

    @PostMapping("/api/books/{id}/decrement")
    void decrementAvailableCopies(@PathVariable Long id);

    @PostMapping("/api/books/{id}/increment")
    void incrementAvailableCopies(@PathVariable Long id);

    @GetMapping("/api/books/{id}/availability")
    boolean isBookAvailable(@PathVariable Long id);
}