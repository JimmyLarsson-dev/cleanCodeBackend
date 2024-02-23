package com.example.cleancode.employees;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "salary")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class Salary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int hourlySalary;
    private double workedHours;

    @OneToOne
    @MapsId
    @JoinColumn(name = "employee_id")
    private Employee employee;

    public Salary(int hourlySalary) {
        this.hourlySalary = hourlySalary;
    }
}
