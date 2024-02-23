package com.example.cleancode.exceptions;

public class JobDoesNotExistException extends RuntimeException{
    public JobDoesNotExistException(String message) {
        super(message);
    }
}
