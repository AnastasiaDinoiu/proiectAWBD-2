package com.library.web.controller;

import com.library.web.service.UserWebService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserWebController {

    private final UserWebService userWebService;

    @GetMapping
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public String listUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        log.info("Listing users - page: {}", page);

        try {
            var userPage = userWebService.getAllUsers(page, size);

            model.addAttribute("userPage", userPage);
            model.addAttribute("currentPage", page);

            return "users/list";
        } catch (Exception e) {
            log.error("Error listing users", e);
            model.addAttribute("error", "Error loading users: " + e.getMessage());
            return "users/list";
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public String viewUser(@PathVariable Long id, Model model) {
        try {
            var user = userWebService.getUserById(id);
            model.addAttribute("user", user);
            return "users/view";
        } catch (Exception e) {
            log.error("Error viewing user", e);
            return "redirect:/users";
        }
    }
}