package com.library.web.service;

import com.library.web.dto.ReviewDTO;
import com.library.web.dto.ReviewPageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class ReviewWebService {

    private final RestTemplate restTemplate;

    @Value("${api.gateway.url:http://localhost:8080}")
    private String apiGatewayUrl;

    public ReviewPageDTO getAllReviews(int page, int size, String sortBy, String sortDirection) {
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(apiGatewayUrl + "/api/reviews")
                .queryParam("page", page)
                .queryParam("size", size)
                .queryParam("sortBy", sortBy)
                .queryParam("sortDirection", sortDirection);

        return restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ReviewPageDTO>() {}
        ).getBody();
    }

    public ReviewPageDTO getReviewsByBook(Long bookId, int page, int size) {
        String url = UriComponentsBuilder
                .fromHttpUrl(apiGatewayUrl + "/api/reviews/book/" + bookId)
                .queryParam("page", page)
                .queryParam("size", size)
                .toUriString();

        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ReviewPageDTO>() {}
        ).getBody();
    }

    public ReviewPageDTO getReviewsByUser(Long userId, int page, int size) {
        String url = UriComponentsBuilder
                .fromHttpUrl(apiGatewayUrl + "/api/reviews/user/" + userId)
                .queryParam("page", page)
                .queryParam("size", size)
                .toUriString();

        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ReviewPageDTO>() {}
        ).getBody();
    }

    public ReviewDTO createReview(ReviewDTO reviewDTO) {
        return restTemplate.postForObject(
                apiGatewayUrl + "/api/reviews",
                reviewDTO,
                ReviewDTO.class
        );
    }

    public void deleteReview(Long reviewId) {
        restTemplate.delete(apiGatewayUrl + "/api/reviews/" + reviewId);
    }

    public Double getAverageRating(Long bookId) {
        return restTemplate.getForObject(
                apiGatewayUrl + "/api/reviews/book/" + bookId + "/average-rating",
                Double.class
        );
    }
}