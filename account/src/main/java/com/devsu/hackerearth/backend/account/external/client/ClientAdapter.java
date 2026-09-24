package com.devsu.hackerearth.backend.account.external.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import com.devsu.hackerearth.backend.account.exception.ResourceNotFoundException;
import com.devsu.hackerearth.backend.account.exception.NotValidException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class ClientAdapter implements ClientExternalPort {
    private static final Logger logger = LoggerFactory.getLogger(ClientAdapter.class);

    private final RestTemplate restTemplate;

    public ClientAdapter(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    @Override
    public Optional<ClientExternalDto> getClientById(Long clientId){
        logger.info("[getClientById] Init");
        try{
            ClientExternalDto client = this.restTemplate.getForObject("/{id}", ClientExternalDto.class, clientId);
            return Optional.ofNullable(client);
        } catch (HttpClientErrorException.NotFound e){
            return Optional.empty();
        } catch(Exception ex){
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,"No se pudo verificar el cliente.");
        }
    }

    @Override
    public ClientExternalDto getClientByIdAndisActive(Long clientId){
        logger.info("[getClientByIdAndisActive] Init");
        ClientExternalDto client = this.getClientById(clientId)
            .orElseThrow(()-> new ResourceNotFoundException("Client not found"));
        if(!client.isActive()){
            throw new NotValidException("Client must be active");
        }
        return client;
    }
}