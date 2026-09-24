package com.devsu.hackerearth.backend.account.external.client;

import java.util.Optional;

public interface ClientExternalPort {
    Optional<ClientExternalDto> getClientById(Long clientId);
    ClientExternalDto getClientByIdAndisActive(Long clientId);
}