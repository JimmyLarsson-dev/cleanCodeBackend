package com.example.cleancode.booked;

import com.example.cleancode.employees.Employee;
import com.example.cleancode.enums.TimeSlots;
import com.example.cleancode.job.Job;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
public class Booked {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date date;

    @Enumerated(EnumType.STRING)
    private TimeSlots timeSlots;

    @ManyToMany
    private List<Employee> employees;

    @ManyToMany
    @JsonManagedReference
    private List<Job> jobs = new ArrayList<>();

    public void addEmployee(Employee employee) {
        if (this.employees == null) {
            this.employees = new ArrayList<>();
        }
        this.employees.add(employee);
    }
}
