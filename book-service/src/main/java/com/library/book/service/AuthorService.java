package com.library.book.service;

import com.library.book.entity.Author;
import com.library.book.exception.ResourceNotFoundException;
import com.library.book.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthorService {

    private static final Logger log = LoggerFactory.getLogger(AuthorService.class);
    private final AuthorRepository authorRepository;

    public List<Author> findAll() {
        log.debug("Finding all authors");
        return authorRepository.findAll();
    }

    public Page<Author> findAll(Pageable pageable) {
        log.debug("Finding all authors with pagination");
        return authorRepository.findAll(pageable);
    }

    public Author findById(Long id) {
        log.debug("Finding author by id: {}", id);
        return authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id));
    }

    public Author save(Author author) {
        log.debug("Saving author: {} {}", author.getFirstName(), author.getLastName());
        return authorRepository.save(author);
    }

    public Author update(Long id, Author author) {
        log.debug("Updating author with id: {}", id);
        Author existingAuthor = findById(id);

        existingAuthor.setFirstName(author.getFirstName());
        existingAuthor.setLastName(author.getLastName());
        existingAuthor.setBiography(author.getBiography());
        existingAuthor.setBirthDate(author.getBirthDate());
        existingAuthor.setNationality(author.getNationality());

        return authorRepository.save(existingAuthor);
    }

    public void deleteById(Long id) {
        log.debug("Deleting author with id: {}", id);
        Author author = findById(id);
        authorRepository.delete(author);
    }

    public Page<Author> searchByName(String name, Pageable pageable) {
        log.debug("Searching authors by name: {}", name);
        return authorRepository.findByFirstNameContainingOrLastNameContaining(name, name, pageable);
    }
}