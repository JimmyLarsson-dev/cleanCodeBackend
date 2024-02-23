package com.example.cleancode.employees;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor


public class EmployeeAuthenticationResponseDTO {

    private String jwt;
    private String userId;

}
