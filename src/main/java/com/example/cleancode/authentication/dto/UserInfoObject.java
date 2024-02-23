package com.example.cleancode.authentication.dto;

import lombok.Data;

@Data
public class UserInfoObject {

    String id;
    String createdTimestamp;
    String username;
    boolean enabled;
    boolean totp;
    boolean emailVerified;
    String firstName;
    String lastName;
    String email;
    Object[] disableableCredentialTypes;
    Object[] requiredActions;
    long notBefore;
    Access access;

}
