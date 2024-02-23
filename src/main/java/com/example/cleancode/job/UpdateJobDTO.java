package com.example.cleancode.job;

import com.example.cleancode.enums.JobStatus;
import com.example.cleancode.enums.Jobtype;
import com.example.cleancode.enums.PaymentOption;
import com.example.cleancode.enums.TimeSlots;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class UpdateJobDTO {

    private long jobId;
    private Jobtype jobtype;
    private Date date;
    private List<TimeSlots> timeSlotsList;
    private JobStatus jobStatus;
    private int squareMeters;
    private PaymentOption paymentOption;
    private String message;
    private UUID customerId;
    private int rating;
}
