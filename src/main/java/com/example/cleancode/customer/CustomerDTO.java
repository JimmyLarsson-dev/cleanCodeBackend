package com.example.cleancode.customer;

import com.example.cleancode.enums.CustomerType;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
public class CustomerDTO {

    UUID id;
    String firstName;
    String lastName;
    String companyName;
    String orgNumber;
    String email;
    String phoneNumber;
    String address;
    String city;
    String postalCode;
    CustomerType customerType;

}
