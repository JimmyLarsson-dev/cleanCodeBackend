package com.example.cleancode.authentication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Credentials {
    String type;
    String value;
    boolean temporary;
}
