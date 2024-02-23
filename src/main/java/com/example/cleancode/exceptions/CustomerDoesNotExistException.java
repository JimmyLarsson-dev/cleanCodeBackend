package com.example.cleancode.exceptions;

import java.util.UUID;

public class CustomerDoesNotExistException extends RuntimeException{
    public CustomerDoesNotExistException(UUID id){
        super("Customer with ID: " + id + " does not exist.");
    }
}
