package com.example.cleancode.employees;

import com.example.cleancode.enums.Role;
import lombok.Data;

@Data
public class EditEmployeeDTO {

    private String firstName;
    private String lastName;
    private String password;
    private String ssNumber;
    private String email;
    private String phoneNumber;
    private String address;
    private String city;
    private String postalCode;
    private Role role;
    private int hourlySalary;

}
