package com.library.book.controller;

import com.library.entity.Publisher;
import com.library.service.PublisherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@RequestMapping("/api/publishers")
@RequiredArgsConstructor
@Slf4j
public class PublisherController {

    private final PublisherService publisherService;

    @GetMapping
    public String listPublishers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(required = false) String search,
            Model model) {

        log.info("Listing publishers - page: {}, size: {}", page, size);

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortBy));
        Page<Publisher> publisherPage;

        if (search != null && !search.isEmpty()) {
            publisherPage = publisherService.searchByName(search, pageRequest);
            model.addAttribute("search", search);
        } else {
            publisherPage = publisherService.findAll(pageRequest);
        }

        model.addAttribute("publisherPage", publisherPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("sortBy", sortBy);

        return "publishers/list";
    }

    @GetMapping("/{id}")
    public String viewPublisher(@PathVariable Long id, Model model) {
        log.info("Viewing publisher with id: {}", id);

        try {
            Publisher publisher = publisherService.findById(id);
            model.addAttribute("publisher", publisher);
            return "publishers/view";
        } catch (Exception e) {
            log.error("Error viewing publisher", e);
            model.addAttribute("errorMessage", "Publisher not found");
            return "error/404";
        }
    }

    @GetMapping("/new")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public String showCreateForm(Model model) {
        log.info("Showing publisher creation form");
        model.addAttribute("publisher", new Publisher());
        return "publishers/form";
    }

    @PostMapping("/new")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public String createPublisher(@Valid @ModelAttribute Publisher publisher,
                                  BindingResult result,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {

        log.info("Creating new publisher: {}", publisher.getName());

        if (result.hasErrors()) {
            return "publishers/form";
        }

        try {
            Publisher savedPublisher = publisherService.save(publisher);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Publisher created successfully!");
            return "redirect:/publishers/" + savedPublisher.getId();
        } catch (Exception e) {
            log.error("Error creating publisher", e);
            model.addAttribute("errorMessage", "Error creating publisher: " + e.getMessage());
            return "publishers/form";
        }
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public String showEditForm(@PathVariable Long id, Model model) {
        log.info("Showing edit form for publisher: {}", id);

        try {
            Publisher publisher = publisherService.findById(id);
            model.addAttribute("publisher", publisher);
            return "publishers/form";
        } catch (Exception e) {
            log.error("Error loading publisher for edit", e);
            model.addAttribute("errorMessage", "Publisher not found");
            return "error/404";
        }
    }

    @PostMapping("/{id}/edit")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public String updatePublisher(@PathVariable Long id,
                                  @Valid @ModelAttribute Publisher publisher,
                                  BindingResult result,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {

        log.info("Updating publisher: {}", id);

        if (result.hasErrors()) {
            return "publishers/form";
        }

        try {
            publisherService.update(id, publisher);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Publisher updated successfully!");
            return "redirect:/publishers/" + id;
        } catch (Exception e) {
            log.error("Error updating publisher", e);
            model.addAttribute("errorMessage", "Error updating publisher: " + e.getMessage());
            return "publishers/form";
        }
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public String deletePublisher(@PathVariable Long id,
                                  RedirectAttributes redirectAttributes) {

        log.info("Deleting publisher: {}", id);

        try {
            publisherService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Publisher deleted successfully!");
        } catch (Exception e) {
            log.error("Error deleting publisher", e);
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error deleting publisher: " + e.getMessage());
        }

        return "redirect:/publishers";
    }
}