package com.example.cleancode.exceptions;

public class CustomerInfoMissMatchException extends RuntimeException{
    public CustomerInfoMissMatchException(String m){
        super(m);
    }
}
