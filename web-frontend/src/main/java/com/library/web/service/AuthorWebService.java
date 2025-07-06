package com.library.web.service;

import com.library.web.dto.AuthorDTO;
import com.library.web.dto.AuthorPageDTO;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorWebService {

    private final RestTemplate restTemplate;

    @Value("${book.service.url:http://localhost:8081}")
    private String bookServiceUrl;

    public AuthorPageDTO getAllAuthors(int page, int size) {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(bookServiceUrl + "/api/authors")
                    .queryParam("page", page)
                    .queryParam("size", size)
                    .toUriString();
            
            log.info("Calling book service for authors at: {}", url);
            
            ResponseEntity<AuthorPageDTO> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<AuthorPageDTO>() {}
            );
            
            log.info("Book service response status: {}", response.getStatusCode());
            AuthorPageDTO result = response.getBody();
            
            if (result == null) {
                log.warn("Received null response from book service");
                return createEmptyAuthorPage();
            }
            
            log.info("Successfully retrieved {} authors", 
                    result.getContent() != null ? result.getContent().size() : 0);
            
            return result;
            
        } catch (RestClientException e) {
            log.error("Error calling book service for authors: {}", e.getMessage());
            log.debug("Full error details:", e);
            return createEmptyAuthorPage();
        } catch (Exception e) {
            log.error("Unexpected error calling book service for authors: {}", e.getMessage(), e);
            return createEmptyAuthorPage();
        }
    }

    public AuthorPageDTO searchAuthors(String search, int page, int size) {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(bookServiceUrl + "/api/authors/search")
                    .queryParam("name", search)
                    .queryParam("page", page)
                    .queryParam("size", size)
                    .toUriString();
            
            log.info("Searching authors with query '{}' at: {}", search, url);
            
            ResponseEntity<AuthorPageDTO> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<AuthorPageDTO>() {}
            );
            
            AuthorPageDTO result = response.getBody();
            
            if (result == null) {
                log.warn("Received null response from book service for search");
                return createEmptyAuthorPage();
            }
            
            log.info("Found {} authors for search '{}'", 
                    result.getContent() != null ? result.getContent().size() : 0, 
                    search);
            
            return result;
            
        } catch (RestClientException e) {
            log.error("Error searching authors with query '{}': {}", search, e.getMessage());
            return createEmptyAuthorPage();
        } catch (Exception e) {
            log.error("Unexpected error searching authors with query '{}': {}", search, e.getMessage(), e);
            return createEmptyAuthorPage();
        }
    }

    public AuthorDTO getAuthorById(Long id) {
        try {
            String url = bookServiceUrl + "/api/authors/" + id;
            log.info("Calling book service for author id: {}", id);
            
            return restTemplate.getForObject(url, AuthorDTO.class);
            
        } catch (RestClientException e) {
            log.error("Error getting author by id {}: {}", id, e.getMessage());
            return null;
        } catch (Exception e) {
            log.error("Unexpected error getting author by id {}: {}", id, e.getMessage(), e);
            return null;
        }
    }

    public AuthorDTO createAuthor(AuthorDTO authorDTO) {
        try {
            String url = bookServiceUrl + "/api/authors";
            log.info("Creating author at: {}", url);
            
            return restTemplate.postForObject(url, authorDTO, AuthorDTO.class);
            
        } catch (RestClientException e) {
            log.error("Error creating author: {}", e.getMessage());
            throw new RuntimeException("Failed to create author: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error creating author: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create author: " + e.getMessage(), e);
        }
    }

    public AuthorDTO updateAuthor(Long id, AuthorDTO authorDTO) {
        try {
            String url = bookServiceUrl + "/api/authors/" + id;
            log.info("Updating author at: {}", url);
            
            restTemplate.put(url, authorDTO);
            return getAuthorById(id);
            
        } catch (RestClientException e) {
            log.error("Error updating author {}: {}", id, e.getMessage());
            throw new RuntimeException("Failed to update author: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error updating author {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Failed to update author: " + e.getMessage(), e);
        }
    }

    public void deleteAuthor(Long id) {
        try {
            String url = bookServiceUrl + "/api/authors/" + id;
            log.info("Deleting author at: {}", url);
            
            restTemplate.delete(url);
            
        } catch (RestClientException e) {
            log.error("Error deleting author {}: {}", id, e.getMessage());
            throw new RuntimeException("Failed to delete author: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error deleting author {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Failed to delete author: " + e.getMessage(), e);
        }
    }

    private AuthorPageDTO createEmptyAuthorPage() {
        AuthorPageDTO emptyPage = new AuthorPageDTO();
        emptyPage.setContent(java.util.Collections.emptyList());
        emptyPage.setTotalElements(0);
        emptyPage.setTotalPages(0);
        emptyPage.setNumber(0);
        emptyPage.setSize(10);
        emptyPage.setEmpty(true);
        emptyPage.setFirst(true);
        emptyPage.setLast(true);
        return emptyPage;
    }
}