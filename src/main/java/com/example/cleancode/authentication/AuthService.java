package com.example.cleancode.authentication;

import com.example.cleancode.authentication.dto.TokenRequestObject;
import com.example.cleancode.customer.Customer;
import com.example.cleancode.customer.CustomerAuthenticationResponseDTO;
import com.example.cleancode.customer.CustomerRepository;
import com.example.cleancode.employees.Employee;
import com.example.cleancode.employees.EmployeeRepository;
import com.example.cleancode.exceptions.HttpRequestFailedException;
import com.example.cleancode.exceptions.PersonDoesNotExistException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private final EmployeeRepository employeeRepository;
    private final CustomerRepository customerRepository;
    private final KeycloakService keycloakService;

    public AuthService(EmployeeRepository employeeRepository, CustomerRepository customerRepository, KeycloakService keycloakService) {
        this.employeeRepository = employeeRepository;
        this.customerRepository = customerRepository;
        this.keycloakService = keycloakService;
    }

    public CustomerAuthenticationResponseDTO loginCustomer(AuthDTO authDTO) {

        Optional<Customer> optCustomer = customerRepository.findByEmail(authDTO.getEmail());

        if (optCustomer.isEmpty()) {
            throw new PersonDoesNotExistException("Email not found");
        }

        ResponseEntity<TokenRequestObject> response = keycloakService.getUserToken(authDTO.getEmail(), authDTO.getPassword());
        String userId = optCustomer.get().getId().toString();
        return new CustomerAuthenticationResponseDTO(response, userId);
    }

    public AuthResponseDTO loginEmployee(AuthDTO authDTO) {
        Optional<Employee> optEmployee = employeeRepository.findByEmail(authDTO.getEmail());

        if (optEmployee.isEmpty()) {
            throw new PersonDoesNotExistException("Email not found");
        }
        System.out.println(authDTO.getEmail() + "  :::::::  " + authDTO.getPassword());

        ResponseEntity<TokenRequestObject> response;
        try {
            response = keycloakService.getUserToken(authDTO.getEmail(), authDTO.getPassword());

        } catch (HttpRequestFailedException e) {
            throw new HttpRequestFailedException("Something went wrong logging in");
        }

        return new AuthResponseDTO(
                optEmployee.get().getId(),
                optEmployee.get().getRole(),
                response);
    }

    public String logout(String id) {

        if(id.length() > 5) {
         //customer

            Optional<Customer> optCust = customerRepository.findById(UUID.fromString(id));
            if(optCust.isPresent()) {

                String adminToken = keycloakService.getAdminToken();
                String userId = keycloakService.getUserId(optCust.get().getEmail(), adminToken);
                String logoutResponse = keycloakService.logoutUser(userId);

                return logoutResponse + "logged out";
            } else {
                return "unknown user";
            }

        } else {
            //employee
            Optional<Employee> optEmp = employeeRepository.findById(Long.valueOf(id));

            if(optEmp.isPresent()) {
                String adminToken = keycloakService.getAdminToken();
                String userId = keycloakService.getUserId(optEmp.get().getEmail(), adminToken);
                String logoutResponse = keycloakService.logoutUser(userId);

                return logoutResponse + "logged out";
            }else {
                return "unknown user";
            }
        }
    }

    public TokenRequestObject getUserTokenRefresh(String refreshToken) {

        return keycloakService.getUserTokenRefresh(refreshToken).getBody();


    }
}
