package com.example.cleancode.authentication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateUserDTO {

    private String email;
    private String firstName;
    private String lastName;
    private String password;
}
