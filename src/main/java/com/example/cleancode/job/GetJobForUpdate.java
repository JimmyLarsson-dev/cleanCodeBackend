package com.example.cleancode.job;

import com.example.cleancode.enums.JobStatus;
import com.example.cleancode.enums.Jobtype;
import com.example.cleancode.enums.PaymentOption;
import com.example.cleancode.enums.TimeSlots;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
public class GetJobForUpdate {
    private long jobId;
    private Jobtype jobtype;
    private Date date;
    private TimeSlots timeSlot;
    private JobStatus jobStatus;
    private int squareMeters;
    private PaymentOption paymentOption;
    private String message;
    private UUID customerId;
}
