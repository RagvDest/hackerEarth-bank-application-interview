package com.devsu.hackerearth.backend.client.external.account;

import java.util.Optional;

public interface AccountExternalPort {
    boolean checkClientHasAccounts(Long clientId);
}