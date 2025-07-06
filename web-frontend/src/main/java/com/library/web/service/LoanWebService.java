package com.library.web.service;

import com.library.web.dto.LoanDTO;
import com.library.web.dto.LoanPageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoanWebService {

    private final RestTemplate restTemplate;

    @Value("${service.loan.url:http://localhost:8083}")
    private String loanServiceUrl;

    public LoanPageDTO getAllLoans(int page, int size, String sortBy, 
                                  String sortDirection, String status) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(loanServiceUrl + "/api/loans")
                    .queryParam("page", page)
                    .queryParam("size", size)
                    .queryParam("sortBy", sortBy)
                    .queryParam("sortDirection", sortDirection);
                
            if (status != null && !status.isEmpty()) {
                builder.queryParam("status", status);
            }

            log.info("Calling loan service at: {}", builder.toUriString());
            
            LoanPageDTO result = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<LoanPageDTO>() {}
            ).getBody();
            
            if (result == null) {
                log.warn("Loan service returned null, creating empty page");
                return createEmptyLoanPage();
            }
            
            return result;
            
        } catch (Exception e) {
            log.error("Error calling loan service: {}", e.getMessage());
            return createEmptyLoanPage();
        }
    }

    public LoanDTO createLoan(LoanDTO loanDTO) {
        try {
            log.info("Creating loan via service: {}", loanServiceUrl);
            return restTemplate.postForObject(
                    loanServiceUrl + "/api/loans", 
                    loanDTO, 
                    LoanDTO.class
            );
        } catch (Exception e) {
            log.error("Error creating loan: {}", e.getMessage());
            throw new RuntimeException("Failed to create loan: " + e.getMessage());
        }
    }

    public LoanDTO getLoan(Long id) {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(loanServiceUrl + "/api/loans/" + id)
                    .toUriString();
            
            log.info("Calling loan service to get loan by id: {}", id);
            
            ResponseEntity<LoanDTO> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    LoanDTO.class
            );
            
            LoanDTO result = response.getBody();
            log.info("Successfully retrieved loan by id: {}", id);
            
            return result;
            
        } catch (RestClientException e) {
            log.error("Error calling loan service for id {}: {}", id, e.getMessage());
            return null;
        } catch (Exception e) {
            log.error("Unexpected error calling loan service for id {}: {}", id, e.getMessage(), e);
            return null;
        }
    }

    public void returnBook(Long loanId) {
        try {
            log.info("Returning book for loan: {}", loanId);
            restTemplate.postForObject(
                    loanServiceUrl + "/api/loans/" + loanId + "/return", 
                    null, 
                    Void.class
            );
        } catch (Exception e) {
            log.error("Error returning book: {}", e.getMessage());
            throw new RuntimeException("Failed to return book: " + e.getMessage());
        }
    }

    public LoanPageDTO getLoansByUser(Long userId, int page, int size) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(loanServiceUrl + "/api/loans/user/" + userId)
                    .queryParam("page", page)
                    .queryParam("size", size);

            log.info("Getting loans for user {} at: {}", userId, builder.toUriString());
            
            LoanPageDTO result = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<LoanPageDTO>() {}
            ).getBody();
            
            return result != null ? result : createEmptyLoanPage();
            
        } catch (Exception e) {
            log.error("Error getting user loans: {}", e.getMessage());
            return createEmptyLoanPage();
        }
    }

    public void updateOverdueLoans() {
        try {
            log.info("Updating overdue loans");
            restTemplate.postForObject(
                    loanServiceUrl + "/api/loans/update-overdue", 
                    null, 
                    Void.class
            );
        } catch (Exception e) {
            log.error("Error updating overdue loans: {}", e.getMessage());
            throw new RuntimeException("Failed to update overdue loans: " + e.getMessage());
        }
    }

    private HttpHeaders createAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        return headers;
    }

    private LoanPageDTO createEmptyLoanPage() {
        LoanPageDTO emptyPage = new LoanPageDTO();
        emptyPage.setContent(Collections.emptyList());
        emptyPage.setTotalElements(0L);
        emptyPage.setTotalPages(0);
        emptyPage.setNumber(0);
        emptyPage.setSize(0);
        emptyPage.setEmpty(true);
        return emptyPage;
    }
}