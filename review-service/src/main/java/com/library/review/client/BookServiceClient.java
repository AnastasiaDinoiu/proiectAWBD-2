package com.library.review.client;

import com.library.review.dto.BookDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "book-service")
public interface BookServiceClient {

    @GetMapping("/api/books/{id}")
    BookDTO findById(@PathVariable Long id);
}