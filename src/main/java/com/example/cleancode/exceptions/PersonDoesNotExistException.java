package com.example.cleancode.exceptions;

public class PersonDoesNotExistException extends RuntimeException{
    public PersonDoesNotExistException(String message) {
        super(message);
    }
}
