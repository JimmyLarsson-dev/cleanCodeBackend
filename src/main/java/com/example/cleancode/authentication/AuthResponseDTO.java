package com.example.cleancode.authentication;

import com.example.cleancode.authentication.dto.TokenRequestObject;
import com.example.cleancode.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;

@AllArgsConstructor
@Data
public class AuthResponseDTO {

    private Long id;
    private Role role;
    private ResponseEntity<TokenRequestObject> response;

}
