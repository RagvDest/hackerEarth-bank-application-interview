package com.devsu.hackerearth.backend.client.external.account;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;

@Configuration
public class AccountRestConfig{

    @Value("${external.services.account-service.url}")
    private String accountServiceUrl;

    @Bean
    public RestTemplate accountServiceRestTemplate(RestTemplateBuilder builder){
        return builder
                .rootUri(accountServiceUrl)
                .defaultHeader("Content-Type","application/json")
                .defaultHeader("Accept","application/json")
                .build();
    }
}