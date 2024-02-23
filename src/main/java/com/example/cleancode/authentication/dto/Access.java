package com.example.cleancode.authentication.dto;

import lombok.Data;

@Data
public class Access {

    boolean manageGroupMembership;
    boolean view;
    boolean mapRoles;
    boolean impersonate;
    boolean manage;
}
