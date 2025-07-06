package com.library.web.controller;

import com.library.web.dto.AuthorDTO;
import com.library.web.dto.AuthorPageDTO;
import com.library.web.service.AuthorWebService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/authors")
@RequiredArgsConstructor
public class AuthorWebController {
    
    private final AuthorWebService authorWebService;

    @GetMapping
    public String listAuthors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "") String search,
            Model model) {
        
        log.info("Listing authors - page: {}, search: {}", page, search);
        
        try {
            AuthorPageDTO authorPage;
            if (search.isEmpty()) {
                authorPage = authorWebService.getAllAuthors(page, 10);
            } else {
                authorPage = authorWebService.searchAuthors(search, page, 10);
            }
            
            if (authorPage == null) {
                authorPage = new AuthorPageDTO();
                authorPage.setContent(new java.util.ArrayList<>());
                authorPage.setTotalElements(0);
                authorPage.setTotalPages(0);
                authorPage.setNumber(0);
                authorPage.setSize(10);
                authorPage.setEmpty(true);
            }
            
            if (authorPage.isEmpty() && authorPage.getTotalElements() == 0) {
                model.addAttribute("message", "No authors found");
                model.addAttribute("messageType", "info");
            }
            
            log.info("Found {} authors total: {}", 
                authorPage.getContent() != null ? authorPage.getContent().size() : 0, 
                authorPage.getTotalElements());
            
            model.addAttribute("authorPage", authorPage);
            model.addAttribute("search", search);
            model.addAttribute("currentPage", page);
            
        } catch (Exception e) {
            log.error("Error loading authors", e);
            model.addAttribute("error", "Error loading authors: " + e.getMessage());
            
            // Create empty page object for error case
            AuthorPageDTO emptyPage = new AuthorPageDTO();
            emptyPage.setContent(new java.util.ArrayList<>());
            emptyPage.setTotalElements(0);
            emptyPage.setTotalPages(0);
            emptyPage.setNumber(0);
            emptyPage.setSize(10);
            emptyPage.setEmpty(true);
            model.addAttribute("authorPage", emptyPage);
        }
        
        return "authors/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        log.info("Showing create author form");
        model.addAttribute("author", new AuthorDTO());
        return "authors/create";
    }

    @PostMapping
    public String createAuthor(@ModelAttribute AuthorDTO author, RedirectAttributes redirectAttributes) {
        log.info("Creating new author: {} {}", author.getFirstName(), author.getLastName());
        
        try {
            AuthorDTO savedAuthor = authorWebService.createAuthor(author);
            log.info("Successfully created author with ID: {}", savedAuthor.getId());
            
            redirectAttributes.addFlashAttribute("message", 
                "Author '" + savedAuthor.getFirstName() + " " + savedAuthor.getLastName() + "' created successfully");
            redirectAttributes.addFlashAttribute("messageType", "success");
            return "redirect:/authors/" + savedAuthor.getId();
            
        } catch (Exception e) {
            log.error("Error creating author", e);
            redirectAttributes.addFlashAttribute("error", "Error creating author: " + e.getMessage());
            return "redirect:/authors/new";
        }
    }

    @GetMapping("/{id}")
    public String showAuthor(@PathVariable Long id, Model model) {
        log.info("Showing author with ID: {}", id);
        
        try {
            AuthorDTO author = authorWebService.getAuthorById(id);
            model.addAttribute("author", author);
            return "authors/view";
            
        } catch (Exception e) {
            log.error("Error loading author with ID: {}", id, e);
            model.addAttribute("error", "Author not found");
            return "authors/list";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        log.info("Showing edit form for author with ID: {}", id);
        
        try {
            AuthorDTO author = authorWebService.getAuthorById(id);
            author.setId(id);
            model.addAttribute("author", author);
            return "authors/edit";
            
        } catch (Exception e) {
            log.error("Error loading author for edit: {}", id, e);
            model.addAttribute("error", "Author not found");
            return "redirect:/authors";
        }
    }

    @PostMapping("/{id}")
    public String updateAuthor(@PathVariable Long id, @ModelAttribute AuthorDTO author, 
                              RedirectAttributes redirectAttributes) {
        log.info("Updating author with ID: {}", id);
        
        try {
            author.setId(id);
            AuthorDTO updatedAuthor = authorWebService.updateAuthor(id, author);
            
            log.info("Successfully updated author: {} {}", 
                updatedAuthor.getFirstName(), updatedAuthor.getLastName());
            
            redirectAttributes.addFlashAttribute("message", "Author updated successfully");
            redirectAttributes.addFlashAttribute("messageType", "success");
            
        } catch (Exception e) {
            log.error("Error updating author", e);
            author.setId(id);
            redirectAttributes.addFlashAttribute("error", "Error updating author: " + e.getMessage());
        }
        
        return "redirect:/authors/" + id;
    }

    @PostMapping("/{id}/delete")
    public String deleteAuthor(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        log.info("Deleting author with ID: {}", id);
        
        try {
            authorWebService.deleteAuthor(id);
            redirectAttributes.addFlashAttribute("message", "Author deleted successfully");
            redirectAttributes.addFlashAttribute("messageType", "success");
            
        } catch (Exception e) {
            log.error("Error deleting author", e);
            redirectAttributes.addFlashAttribute("error", "Error deleting author: " + e.getMessage());
        }
        
        return "redirect:/authors";
    }
}