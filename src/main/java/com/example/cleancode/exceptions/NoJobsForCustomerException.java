package com.example.cleancode.exceptions;

public class NoJobsForCustomerException extends RuntimeException{
    public NoJobsForCustomerException(String message) {
        super(message);
    }
}
