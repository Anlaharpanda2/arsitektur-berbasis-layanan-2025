package com.anla.uts.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiClientService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${api.data.url:http://localhost:8080/api/data}")
    private String dataApiUrl;

    public ResponseEntity<String> sendJsonToDataApi(String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(json, headers);

        return restTemplate.postForEntity(dataApiUrl, entity, String.class);
    }
}
