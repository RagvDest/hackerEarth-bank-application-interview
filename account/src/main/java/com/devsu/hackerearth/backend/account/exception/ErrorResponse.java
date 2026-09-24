package com.devsu.hackerearth.backend.account.exception;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.Map;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private String code;
    private String message;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    private Map<String, String> validations;

    public ErrorResponse(String code, String message){
        this.code = code;
        this.message = message;
    }
    public ErrorResponse(String code, String message, Map<String, String> validations){
        this.code = code;
        this.message = message;
        this.validations = validations;
    }

}