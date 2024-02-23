package com.example.cleancode.authentication.dto;

import lombok.Data;

@Data
public class AssignRoleDTO {

    String role;
    String username;
    String adminToken;

}
