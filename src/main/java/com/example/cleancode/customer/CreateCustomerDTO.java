package com.example.cleancode.customer;

import com.example.cleancode.enums.CustomerType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCustomerDTO {

    private String firstName;
    private String lastName;
    private String password;
    private String companyName;
    private String orgNumber;
    private String email;
    private String phoneNumber;
    private String address;
    private String city;
    private String postalCode;
    private CustomerType customerType;

}
