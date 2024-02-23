package com.example.cleancode.employees;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api/employee")
@CrossOrigin(origins = "*", allowedHeaders = "*")

public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/createEmployee")
    public EmployeeAuthenticationResponseDTO createEmp(@RequestBody CreateEmployeeDTO createDTO){
        return employeeService.createEmp(createDTO);
    }

    @DeleteMapping("/deleteEmployee")
    public Long deleteEmployee(@RequestHeader Long empId) {
        return employeeService.deleteEmployee(empId);
    }

    @GetMapping("/getEmployee")
    public GetEmployeeDTO getEmployee(@RequestHeader Long empId) {
        return employeeService.getEmployee(empId);
    }

    @GetMapping("/getAllEmployees")
    public List<GetEmployeeDTO> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @PutMapping("/editEmployee")
    public GetEmployeeDTO editEmployee(@RequestHeader Long empId, @RequestBody EditEmployeeDTO employee) {

        return employeeService.editEmployee(empId, employee);
    }

    @GetMapping("/getSalary/{id}")
    public SalaryDTO getSalary(@PathVariable Long id) {
        return employeeService.getSalary(id);

    }

}
