package com.example.cleancode.exceptions;

import java.util.UUID;

public class CustomerWithEmailDoesNotExistException extends RuntimeException{
    public CustomerWithEmailDoesNotExistException(String email){
        super("Customer with email: " + email + " does not exist.");
    }
}
