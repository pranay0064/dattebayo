package com.dattebayo.dattebayo.util;

import org.springframework.http.*;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;

public class DattebayoClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String baseUri;
    private final String authToken;
    private Map<String, String> headerMap;

    DattebayoClient(String baseUri, String authToken) {
        this.baseUri = baseUri;
        this.authToken = authToken;
    }

    public DattebayoClient(String baseUri, String authToken, Map<String, String> headerMap) {
        this.baseUri = baseUri;
        this.authToken = authToken;
        this.headerMap = headerMap;
    }

    public String makeHttpRequest(String url, HttpMethod method, String body, Map<String, String> httpHeaders) {
        long start = System.currentTimeMillis();
        try {
            HttpHeaders headers = new HttpHeaders();
            if (!CollectionUtils.isEmpty(httpHeaders)) {
                httpHeaders.forEach(headers::add);
            }

            this.addUniqueHeaderValue(headers, "Accept", "application/json");
            if (!StringUtils.isEmpty(body)) {
                this.addUniqueHeaderValue(headers, "Content-Type", "application/json");
            }
            if (this.authToken != null && !this.authToken.isEmpty()) {
                this.addUniqueHeaderValue(headers, "Authorization", "Bearer " + this.authToken);
            }

            if (this.headerMap != null) {
                for (Map.Entry<String, String> map : this.headerMap.entrySet()) {
                    this.addUniqueHeaderValue(headers, map.getKey(), map.getValue());
                }
                headers.entrySet().stream().forEach(e -> System.out.println("Header Key {} ::::: Value "+ e.getKey()+ e.getValue()));
            }

            RequestEntity<String> request = new RequestEntity(body, headers, method, new URI(this.baseUri + "/" + url));
            System.out.println(" API Request URL: "+ request.getUrl());
            System.out.println(" API Request Body: "+ request.getBody());
            ResponseEntity<String> response = this.restTemplate.exchange(request, String.class);
            System.out.println(" API Execution time of url is  ms"+ url+ (System.currentTimeMillis() - start));
            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println(" API Request Success for url: "+ request.getUrl());
                return (String) response.getBody();
            } else {
                System.out.println(" API Request failure for url: "+ request.getUrl());
                throw new Exception( (String) response.getBody());
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("error in api call");
        }
    }

    private void addUniqueHeaderValue(HttpHeaders httpHeaders, String headerName, String headerValue) {
        httpHeaders.remove(headerName);
        httpHeaders.add(headerName, headerValue);
    }
}
