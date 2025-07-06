package com.library.book.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.library.book.dto.AuthorDTO;
import com.library.book.entity.Author;
import com.library.book.service.AuthorService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
public class AuthorController {

    private static final Logger log = LoggerFactory.getLogger(AuthorController.class);
    private final AuthorService authorService;

    @GetMapping
    public Page<AuthorDTO> getAllAuthors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "lastName") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection,
            @RequestParam(required = false) String search) {

        log.info("Getting authors - page: {}, size: {}, search: {}", page, size, search);

        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC")
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<Author> authorPage;
        if (search != null && !search.isEmpty()) {
            authorPage = authorService.searchByName(search.trim(), pageRequest);
        } else {
            authorPage = authorService.findAll(pageRequest);
        }

        return authorPage.map(this::convertToDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDTO> getAuthorById(@PathVariable Long id) {
        log.info("Getting author with id: {}", id);

        try {
            Author author = authorService.findById(id);
            return ResponseEntity.ok(convertToDTO(author));
        } catch (Exception e) {
            log.error("Author not found with id: {}", id, e);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<AuthorDTO> createAuthor(@Valid @RequestBody AuthorDTO authorDTO) {
        log.info("Creating new author: {} {}", authorDTO.getFirstName(), authorDTO.getLastName());

        try {
            Author author = convertToEntity(authorDTO);
            Author savedAuthor = authorService.save(author);
            return ResponseEntity.ok(convertToDTO(savedAuthor));
        } catch (Exception e) {
            log.error("Error creating author", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorDTO> updateAuthor(@PathVariable Long id, @Valid @RequestBody AuthorDTO authorDTO) {
        log.info("Updating author: {}", id);

        try {
            Author author = convertToEntity(authorDTO);
            author.setId(id);
            Author updatedAuthor = authorService.update(id, author);
            return ResponseEntity.ok(convertToDTO(updatedAuthor));
        } catch (Exception e) {
            log.error("Error updating author", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        log.info("Deleting author: {}", id);

        try {
            authorService.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error deleting author", e);
            return ResponseEntity.badRequest().build();
        }
    }

    private AuthorDTO convertToDTO(Author author) {
        AuthorDTO dto = new AuthorDTO();
        dto.setId(author.getId());
        dto.setFirstName(author.getFirstName());
        dto.setLastName(author.getLastName());
        dto.setBiography(author.getBiography());
        dto.setBirthDate(author.getBirthDate());
        dto.setNationality(author.getNationality());
        return dto;
    }

    private Author convertToEntity(AuthorDTO dto) {
        Author author = new Author();
        author.setId(dto.getId());
        author.setFirstName(dto.getFirstName());
        author.setLastName(dto.getLastName());
        author.setBiography(dto.getBiography());
        author.setBirthDate(dto.getBirthDate());
        author.setNationality(dto.getNationality());
        return author;
    }
}