package com.example.cleancode.employees;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MonthlyResetService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public void resetMonthlyValues() {
        List<Employee> employees = employeeRepository.findAll();

        for (Employee employee : employees) {
            if (employee.getSalary() != null) {
                Salary salary = employee.getSalary();
                salary.setWorkedHours(0);
            }
        }
        employeeRepository.saveAll(employees);
    }
}
