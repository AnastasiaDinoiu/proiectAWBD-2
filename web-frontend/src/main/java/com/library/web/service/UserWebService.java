package com.library.web.service;

import com.library.web.dto.UserDTO;
import com.library.web.dto.UserPageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class UserWebService {

    private final RestTemplate restTemplate;

    @Value("${api.gateway.url:http://localhost:8080}")
    private String apiGatewayUrl;

    public UserPageDTO getAllUsers(int page, int size) {
        String url = UriComponentsBuilder
                .fromHttpUrl(apiGatewayUrl + "/api/users")
                .queryParam("page", page)
                .queryParam("size", size)
                .toUriString();

        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<UserPageDTO>() {}
        ).getBody();
    }

    public UserDTO getUserByUsername(String username) {
        return restTemplate.getForObject(
                apiGatewayUrl + "/api/users/username/" + username,
                UserDTO.class
        );
    }

    public UserDTO getUserById(Long id) {
        return restTemplate.getForObject(
                apiGatewayUrl + "/api/users/" + id,
                UserDTO.class
        );
    }
}