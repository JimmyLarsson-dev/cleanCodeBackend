package com.example.cleancode.exceptions;

public class HttpRequestFailedException extends RuntimeException{
    public HttpRequestFailedException(String message) {
        super(message);
    }
}
