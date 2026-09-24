package com.devsu.hackerearth.backend.account.external.client;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ClientExternalDto {
    private Long id;
    private String name;
    private boolean isActive;
}