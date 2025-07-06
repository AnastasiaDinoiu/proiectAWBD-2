package com.library.book.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "loan-service")
public interface LoanServiceClient {

    @GetMapping("/api/loans/book/{bookId}/active-count")
    Integer getActiveLoanCountByBookId(@PathVariable Long bookId);
}