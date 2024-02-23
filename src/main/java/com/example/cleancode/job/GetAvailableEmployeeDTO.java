package com.example.cleancode.job;

import lombok.Data;

import java.util.Date;

@Data
public class GetAvailableEmployeeDTO {

    private Date date;
    private int lookForAvailableThisManyHours;

}
