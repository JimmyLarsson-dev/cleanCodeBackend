package com.example.cleancode.exceptions;

public class CustomerAlreadyExistsException extends RuntimeException{
    public CustomerAlreadyExistsException(String mail){
        super("Customer with E-mail: " + mail + " already exists!");
    }
}
