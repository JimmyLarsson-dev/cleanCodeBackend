package com.example.cleancode.customer;

import com.example.cleancode.enums.CustomerType;
import lombok.Data;

import java.util.UUID;

@Data
public class EditCustomerDTO {
    UUID id;
    String firstName;
    String lastName;
    String password;
    String companyName;
    String orgNumber;
    String email;
    String phoneNumber;
    String address;
    String city;
    String postalCode;
    CustomerType customerType;
}
