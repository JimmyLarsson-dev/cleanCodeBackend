package com.example.cleancode.customer;

import com.example.cleancode.authentication.dto.TokenRequestObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;

@Data
@AllArgsConstructor
public class CustomerAuthenticationResponseDTO {

    private ResponseEntity<TokenRequestObject> response;
    private String userId;

}
