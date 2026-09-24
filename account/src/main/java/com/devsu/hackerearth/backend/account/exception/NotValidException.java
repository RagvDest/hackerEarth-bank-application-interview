package com.devsu.hackerearth.backend.account.exception;

public class NotValidException extends RuntimeException{
    public NotValidException(String message){
        super(message);
    }
}