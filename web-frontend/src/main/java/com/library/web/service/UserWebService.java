package com.library.web.service;

import com.library.web.dto.UserDTO;
import com.library.web.dto.UserPageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Base64;
import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserWebService {

    private final RestTemplate restTemplate;

    @Value("${user.service.url:http://localhost:8082}")
    private String userServiceUrl;
    
    @Value("${user.service.username:service-user}")
    private String serviceUsername;
    
    @Value("${user.service.password:service-password}")
    private String servicePassword;

    private HttpHeaders createAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String auth = serviceUsername + ":" + servicePassword;
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
        String authHeader = "Basic " + new String(encodedAuth);
        headers.set("Authorization", authHeader);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }

    public UserPageDTO getAllUsers(int page, int size) {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(userServiceUrl + "/api/users")
                    .queryParam("page", page)
                    .queryParam("size", size)
                    .toUriString();
            
            log.info("Calling user service at: {}", url);
            
            HttpEntity<String> entity = new HttpEntity<>(createAuthHeaders());
            
            ResponseEntity<UserPageDTO> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<UserPageDTO>() {}
            );
            
            log.info("User service response status: {}", response.getStatusCode());
            UserPageDTO result = response.getBody();
            
            if (result == null) {
                log.warn("Received null response from user service");
                return createEmptyUserPage();
            }
            
            log.info("Successfully retrieved {} users", 
                    result.getContent() != null ? result.getContent().size() : 0);
            
            return result;
            
        } catch (RestClientException e) {
            log.error("Error calling user service: {}", e.getMessage());
            log.debug("Full error details:", e);
            return createEmptyUserPage();
        } catch (Exception e) {
            log.error("Unexpected error calling user service: {}", e.getMessage(), e);
            return createEmptyUserPage();
        }
    }

    public UserDTO getUserById(Long id) {
        try {
            String url = userServiceUrl + "/api/users/" + id;
            log.info("Calling user service for user id: {}", id);
            
            HttpEntity<String> entity = new HttpEntity<>(createAuthHeaders());
            
            ResponseEntity<UserDTO> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    UserDTO.class
            );
            
            return response.getBody();
            
        } catch (RestClientException e) {
            log.error("Error getting user by id {}: {}", id, e.getMessage());
            return null;
        } catch (Exception e) {
            log.error("Unexpected error getting user by id {}: {}", id, e.getMessage(), e);
            return null;
        }
    }

    public UserDTO getUserByUsername(String username) {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(userServiceUrl + "/api/users/username/" + username)
                    .toUriString();
            
            log.info("Calling user service to get user by username: {}", username);
            
            HttpEntity<String> entity = new HttpEntity<>(createAuthHeaders());
            
            ResponseEntity<UserDTO> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    UserDTO.class
            );
            
            UserDTO result = response.getBody();
            log.info("Successfully retrieved user by username: {}", username);
            
            return result;
            
        } catch (RestClientException e) {
            log.error("Error calling user service for username {}: {}", username, e.getMessage());
            return null;
        } catch (Exception e) {
            log.error("Unexpected error calling user service for username {}: {}", username, e.getMessage(), e);
            return null;
        }
    }

    private UserPageDTO createEmptyUserPage() {
        UserPageDTO emptyPage = new UserPageDTO();
        emptyPage.setContent(Collections.emptyList());
        emptyPage.setEmpty(true);
        emptyPage.setTotalPages(0);
        emptyPage.setTotalElements(0);
        return emptyPage;
    }
}