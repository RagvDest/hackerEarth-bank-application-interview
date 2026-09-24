package com.devsu.hackerearth.backend.account.external.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;

@Configuration
public class ClientRestConfig{

    @Value("${external.services.client-service.url}")
    private String clientServiceUrl;

    @Bean
    public RestTemplate clientServiceRestTemplate(RestTemplateBuilder builder){
        return builder
                .rootUri(clientServiceUrl)
                .defaultHeader("Content-Type","application/json")
                .defaultHeader("Accept","application/json")
                .build();
    }
}