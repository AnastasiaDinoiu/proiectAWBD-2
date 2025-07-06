package com.library.web.service;

import com.library.web.dto.BookDTO;
import com.library.web.dto.BookPageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookWebService {

    private final RestTemplate restTemplate;

    @Value("${api.gateway.url:http://localhost:8080}")
    private String apiGatewayUrl;

    public BookPageDTO getAllBooks(int page, int size, String search) {
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(apiGatewayUrl + "/api/books")
                .queryParam("page", page)
                .queryParam("size", size);

        if (search != null && !search.isEmpty()) {
            builder.queryParam("search", search);
        }

        return restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<BookPageDTO>() {}
        ).getBody();
    }

    public List<BookDTO> getAllBooks() {
        return restTemplate.exchange(
                apiGatewayUrl + "/api/books?size=1000",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Page<BookDTO>>() {}
        ).getBody().getContent();
    }

    public BookDTO getBookById(Long id) {
        return restTemplate.getForObject(
                apiGatewayUrl + "/api/books/" + id,
                BookDTO.class
        );
    }

    public List<BookDTO> getRecentBooks(int limit) {
        return restTemplate.exchange(
                apiGatewayUrl + "/api/books?size=" + limit + "&sort=id,desc",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Page<BookDTO>>() {}
        ).getBody().getContent();
    }

    public BookDTO saveBook(BookDTO bookDTO) {
        if (bookDTO.getId() != null) {
            restTemplate.put(apiGatewayUrl + "/api/books/" + bookDTO.getId(), bookDTO);
            return bookDTO;
        } else {
            return restTemplate.postForObject(
                    apiGatewayUrl + "/api/books",
                    bookDTO,
                    BookDTO.class
            );
        }
    }
}