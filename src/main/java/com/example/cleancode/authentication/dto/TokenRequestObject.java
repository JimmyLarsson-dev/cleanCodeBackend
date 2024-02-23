package com.example.cleancode.authentication.dto;

import lombok.Data;

@Data
public class TokenRequestObject {

     String access_token;
     int expires_in;
     int refresh_expires_in;
     String refresh_token;
     String token_type;
     int not_before_policy;
     String session_State;
     String scope;



}
