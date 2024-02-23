package com.example.cleancode.authentication.dto;

import lombok.Data;

@Data
public class UpdateUserInfoKeycloakDTO {

    private String firstName;
    private String lastName;
    private String email;
//    private CredentialsUpdate[] credentials;
}