package com.library.web.service;

import com.library.web.dto.LoanDTO;
import com.library.web.dto.LoanPageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class LoanWebService {

    private final RestTemplate restTemplate;
    
    @Value("${api.gateway.url:http://localhost:8080}")
    private String apiGatewayUrl;

    public LoanPageDTO getAllLoans(int page, int size, String sortBy, 
                                  String sortDirection, String status) {
        
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(apiGatewayUrl + "/api/loans")
                .queryParam("page", page)
                .queryParam("size", size)
                .queryParam("sortBy", sortBy)
                .queryParam("sortDirection", sortDirection);
                
        if (status != null && !status.isEmpty()) {
            builder.queryParam("status", status);
        }

        return restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<LoanPageDTO>() {}
        ).getBody();
    }

    public LoanPageDTO getLoansByUser(Long userId, int page, int size) {
        String url = UriComponentsBuilder
                .fromHttpUrl(apiGatewayUrl + "/api/loans/user/" + userId)
                .queryParam("page", page)
                .queryParam("size", size)
                .toUriString();

        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<LoanPageDTO>() {}
        ).getBody();
    }

    public LoanDTO createLoan(LoanDTO loanDTO) {
        return restTemplate.postForObject(
                apiGatewayUrl + "/api/loans",
                loanDTO,
                LoanDTO.class
        );
    }

    public LoanDTO returnBook(Long loanId) {
        return restTemplate.postForObject(
                apiGatewayUrl + "/api/loans/" + loanId + "/return",
                null,
                LoanDTO.class
        );
    }
}