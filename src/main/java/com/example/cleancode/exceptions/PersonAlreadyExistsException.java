package com.example.cleancode.exceptions;

public class PersonAlreadyExistsException extends RuntimeException{
    public PersonAlreadyExistsException(String message) {
        super(message);
    }
}
