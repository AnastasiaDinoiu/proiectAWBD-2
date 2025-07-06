package com.library.web.controller;

import com.library.web.service.BookWebService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;

@Controller
@RequestMapping("/reports")
@RequiredArgsConstructor
@Slf4j
public class ReportWebController {

    private final BookWebService bookWebService;

    @GetMapping("/available")
    @PreAuthorize("hasRole('ADMIN')")
    public String availableBooks(Model model) {
        log.info("Loading available books report");

        try {
            var bookPage = bookWebService.getAllBooks(0, 1000, "");

            if (bookPage != null && bookPage.getContent() != null) {
                var availableBooks = bookPage.getContent().stream()
                        .filter(book -> {
                            Integer copies = book.getAvailableCopies();
                            return copies != null && copies > 0;
                        })
                        .collect(java.util.stream.Collectors.toList());

                model.addAttribute("books", availableBooks);
                model.addAttribute("totalAvailable", availableBooks.size());

                log.info("Found {} available books out of {} total books",
                        availableBooks.size(), bookPage.getContent().size());
            } else {
                model.addAttribute("books", Collections.emptyList());
                model.addAttribute("totalAvailable", 0);
            }

        } catch (Exception e) {
            log.error("Error loading available books", e);
            model.addAttribute("books", Collections.emptyList());
            model.addAttribute("totalAvailable", 0);
            model.addAttribute("error", "Could not load available books: " + e.getMessage());
        }

        return "reports/available";
    }

    @GetMapping("/inventory-report")
    @PreAuthorize("hasRole('ADMIN')")
    public String inventoryReport(Model model) {
        log.info("Loading inventory report");

        try {
            var bookPage = bookWebService.getAllBooks(0, 100, "");

            // Date simple pentru raport
            model.addAttribute("lowStockBooks", Collections.emptyList());
            model.addAttribute("statistics", Collections.emptyMap());

        } catch (Exception e) {
            log.error("Error loading inventory report", e);
            model.addAttribute("lowStockBooks", Collections.emptyList());
            model.addAttribute("statistics", Collections.emptyMap());
            model.addAttribute("error", "Could not load inventory data");
        }

        return "reports/inventory-report";
    }
}