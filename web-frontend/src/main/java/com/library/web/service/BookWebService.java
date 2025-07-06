package com.library.web.service;

import com.library.web.dto.BookDTO;
import com.library.web.dto.BookPageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookWebService {

    private final RestTemplate restTemplate;

    @Value("${book.service.url:http://localhost:8081}")
    private String bookServiceUrl;

    public BookPageDTO getAllBooks(int page, int size, String search) {
        try {
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(bookServiceUrl + "/api/books")
                    .queryParam("page", page)
                    .queryParam("size", size);
            
            if (search != null && !search.trim().isEmpty()) {
                uriBuilder.queryParam("search", search.trim());
            }
            
            String url = uriBuilder.toUriString();
            log.info("Calling book service at: {}", url);
            
            ResponseEntity<BookPageDTO> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<BookPageDTO>() {}
            );
            
            BookPageDTO result = response.getBody();
            
            if (result == null) {
                log.warn("Received null response from book service");
                return createEmptyBookPage();
            }
            
            log.info("Successfully retrieved {} books", 
                    result.getContent() != null ? result.getContent().size() : 0);
            
            return result;
            
        } catch (RestClientException e) {
            log.error("Error calling book service: {}", e.getMessage());
            log.debug("Full error details:", e);
            return createEmptyBookPage();
        } catch (Exception e) {
            log.error("Unexpected error calling book service: {}", e.getMessage(), e);
            return createEmptyBookPage();
        }
    }

    private BookPageDTO createEmptyBookPage() {
        BookPageDTO emptyPage = new BookPageDTO();
        emptyPage.setContent(Collections.emptyList());
        emptyPage.setEmpty(true);
        emptyPage.setTotalPages(0);
        emptyPage.setTotalElements(0);
        return emptyPage;
    }

    public List<BookDTO> getAllBooks() {
        try {
            String url = bookServiceUrl + "/api/books/all";
            log.info("Getting all books from: {}", url);
            
            ResponseEntity<List<BookDTO>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<BookDTO>>() {}
            );
            
            return response.getBody() != null ? response.getBody() : Collections.emptyList();
            
        } catch (RestClientException e) {
            log.error("Error getting all books: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public BookDTO getBookById(Long id) {
        try {
            String url = bookServiceUrl + "/api/books/" + id;
            log.info("Getting book by id from: {}", url);
            
            return restTemplate.getForObject(url, BookDTO.class);
            
        } catch (RestClientException e) {
            log.error("Error getting book by id {}: {}", id, e.getMessage());
            return null;
        }
    }

    public List<BookDTO> getAvailableBooks() {
        try {
            String url = bookServiceUrl + "/api/books/available";
            log.info("Getting available books from: {}", url);
            
            ResponseEntity<List<BookDTO>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<BookDTO>>() {}
            );
            
            return response.getBody() != null ? response.getBody() : Collections.emptyList();
            
        } catch (RestClientException e) {
            log.error("Error getting available books: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<BookDTO> searchBooks(String query) {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(bookServiceUrl + "/api/books/search")
                    .queryParam("q", query)
                    .toUriString();
            
            log.info("Searching books with query '{}' at: {}", query, url);
            
            ResponseEntity<List<BookDTO>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<BookDTO>>() {}
            );
            
            return response.getBody() != null ? response.getBody() : Collections.emptyList();
            
        } catch (RestClientException e) {
            log.error("Error searching books with query '{}': {}", query, e.getMessage());
            return Collections.emptyList();
        }
    }

    public BookDTO saveBook(BookDTO bookDTO) {
        try {
            String url = bookServiceUrl + "/api/books";
            log.info("Saving book to: {}", url);
            
            return restTemplate.postForObject(url, bookDTO, BookDTO.class);
            
        } catch (RestClientException e) {
            log.error("Error saving book: {}", e.getMessage());
            throw new RuntimeException("Failed to save book: " + e.getMessage(), e);
        }
    }

    public BookDTO updateBook(Long id, BookDTO bookDTO) {
        try {
            String url = bookServiceUrl + "/api/books/" + id;
            log.info("Updating book at: {}", url);
            
            restTemplate.put(url, bookDTO);
            return getBookById(id);
            
        } catch (RestClientException e) {
            log.error("Error updating book {}: {}", id, e.getMessage());
            throw new RuntimeException("Failed to update book: " + e.getMessage(), e);
        }
    }

    public void deleteBook(Long id) {
        try {
            String url = bookServiceUrl + "/api/books/" + id;
            log.info("Deleting book at: {}", url);
            
            restTemplate.delete(url);
            
        } catch (RestClientException e) {
            log.error("Error deleting book {}: {}", id, e.getMessage());
            throw new RuntimeException("Failed to delete book: " + e.getMessage(), e);
        }
    }
}