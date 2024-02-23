package com.example.cleancode.authentication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateUserRequest {

    boolean enabled;
    String email;
    String firstName;
    String lastName;
    String username;
    Credentials[] credentials;
}
