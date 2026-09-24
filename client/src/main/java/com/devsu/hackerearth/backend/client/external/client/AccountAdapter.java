package com.devsu.hackerearth.backend.client.external.account;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.client.HttpClientErrorException;
import com.devsu.hackerearth.backend.client.exception.ResourceNotFoundException;
import com.devsu.hackerearth.backend.client.exception.NotValidException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class AccountAdapter implements AccountExternalPort {
    private static final Logger logger = LoggerFactory.getLogger(AccountAdapter.class);

    private final RestTemplate restTemplate;

    public AccountAdapter(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    @Override
    public boolean checkClientHasAccounts(Long clientId) {
        try {
            logger.info("[AccountAdapter] Invoking Account microservice");
            AccountCheckResponse response = restTemplate.getForObject("/client/{clientId}/exists",
                AccountCheckResponse.class,
                clientId
            );
            return response != null && response.isHasAccounts();
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,"No se pudo verificar el estado de las cuentas del cliente.");
        }
    }
}