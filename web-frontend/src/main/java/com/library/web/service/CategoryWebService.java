package com.library.web.service;

import com.library.web.dto.CategoryDTO;
import com.library.web.dto.CategoryPageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryWebService {

    private final RestTemplate restTemplate;

    @Value("${book.service.url:http://localhost:8081}")
    private String bookServiceUrl;

    public CategoryPageDTO getAllCategories(int page, int size, String sortBy, String sortDirection, String search) {
        try {
            String url = UriComponentsBuilder
                    .fromHttpUrl(bookServiceUrl + "/api/categories")
                    .queryParam("page", page)
                    .queryParam("size", size)
                    .queryParam("sortBy", sortBy)
                    .queryParam("sortDirection", sortDirection)
                    .queryParamIfPresent("search", search != null && !search.trim().isEmpty() ?
                            java.util.Optional.of(search) : java.util.Optional.empty())
                    .toUriString();

            return restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<CategoryPageDTO>() {}
            ).getBody();
        } catch (Exception e) {
            log.error("Error fetching categories from API", e);
            // Returnează o pagină goală în loc să arunce excepție
            CategoryPageDTO emptyPage = new CategoryPageDTO();
            emptyPage.setContent(new ArrayList<>());
            emptyPage.setTotalElements(0);
            emptyPage.setTotalPages(0);
            return emptyPage;
        }
    }

    public List<CategoryDTO> getAllCategories() {
        try {
            return restTemplate.exchange(
                    bookServiceUrl + "/api/categories/all",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<CategoryDTO>>() {}
            ).getBody();
        } catch (Exception e) {
            log.error("Error fetching all categories", e);
            return new ArrayList<>();
        }
    }

    public CategoryDTO getCategoryById(Long id) {
        return restTemplate.getForObject(
                bookServiceUrl + "/api/categories/" + id,
                CategoryDTO.class
        );
    }

    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        return restTemplate.postForObject(
                bookServiceUrl + "/api/categories",
                categoryDTO,
                CategoryDTO.class
        );
    }

    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        restTemplate.put(
                bookServiceUrl + "/api/categories/" + id,
                categoryDTO
        );
        return getCategoryById(id);
    }

    public void deleteCategory(Long id) {
        restTemplate.delete(bookServiceUrl + "/api/categories/" + id);
    }
}