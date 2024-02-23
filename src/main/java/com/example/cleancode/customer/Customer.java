package com.example.cleancode.customer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.example.cleancode.enums.CustomerType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Customer {
    @Id
    private UUID id;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "customer_type")
    private CustomerType customerType;

    public void setPassword(String rawPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.password = passwordEncoder.encode(rawPassword);
    }

    @PrePersist
    public void encryptPassword() {
        setPassword(this.password);
    }

    public Customer(UUID id, String firstName, String lastName, String password, String companyName, String orgNumber, String email, String phoneNumber, String address, String city, String postalCode, CustomerType customerType) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.companyName = companyName;
        this.orgNumber = orgNumber;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.city = city;
        this.postalCode = postalCode;
        this.customerType = customerType;
    }
    public Customer(UUID id, String firstName, String lastName, String password, String email, String phoneNumber, String address,  String city, String postalCode, CustomerType customerType) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.city = city;
        this.postalCode = postalCode;
        this.customerType = customerType;
    }
}
