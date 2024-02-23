package com.example.cleancode.employees;

import com.example.cleancode.authentication.KeycloakService;
import com.example.cleancode.authentication.dto.CreateUserDTO;
import com.example.cleancode.authentication.dto.CredentialsUpdate;
import com.example.cleancode.authentication.dto.UpdateUserInfoKeycloakDTO;
import com.example.cleancode.customer.CustomerAuthenticationResponseDTO;
import com.example.cleancode.enums.CustomerType;
import com.example.cleancode.exceptions.*;
import com.example.cleancode.mail.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final KeycloakService keycloakService;

    public EmployeeService(EmployeeRepository employeeRepository, KeycloakService keycloakService) {
        this.employeeRepository = employeeRepository;
        this.keycloakService = keycloakService;
    }

    public Long deleteEmployee(Long id) {
        /** Deletes the employee with the entered id. If no such employee exist, we throw an exception. Returns the deleted employees id.*/
        Optional<Employee> optEmp = employeeRepository.findById(id);
        if (optEmp.isPresent()) {

            String adminToken = keycloakService.getAdminToken();
            String userId = keycloakService.getUserId(optEmp.get().getEmail(), adminToken);

            String deleteResponse = keycloakService.deleteUser(userId, adminToken);


            if (deleteResponse.contains("204")) {
                employeeRepository.deleteById(id);
                return id;
            }
            return 0L;

        } else {
            throw new PersonDoesNotExistException("There is no employee with that id in database.");
        }
    }

    public GetEmployeeDTO getEmployee(Long empId) {
        /** Returns DTO with the employee with the entered id. If no such employee exist, we throw an exception. */
        Optional<Employee> optEmp = employeeRepository.findById(empId);
        if (optEmp.isPresent()) {
            return employeeToGetEmployeeDto(optEmp.get());
        } else {
            throw new PersonDoesNotExistException("No employee with that id was found!");
        }
    }

    public List<GetEmployeeDTO> getAllEmployees() {
        /** Returns a list, containing all employees, including Admins */
        List<Employee> empList = employeeRepository.findAll();
        return empList
                .stream()
                .map(emp -> employeeToGetEmployeeDto(emp))
                .collect(Collectors.toList());
    }

    public GetEmployeeDTO editEmployee(Long empId, EditEmployeeDTO employeeDTO) {
        /** If an employee with the entered id exists, we update it with the values from the DTO. If not, exception is thrown. */
        Optional<Employee> optEmp = employeeRepository.findById(empId);

        if (!checkEditEmployeeDTO(employeeDTO)) {
            throw new InvalidRequestException("Some fields had missing or invalid data");
        }

        System.out.println(employeeDTO);

        if (optEmp.isPresent()) {

            String tempPassword;

            if(employeeDTO.getPassword() == null || employeeDTO.getPassword().isEmpty() || employeeDTO.getPassword().length() < 2) {
                tempPassword = optEmp.get().getPassword();


                /** Gör en koll om password stämmer med det i databasen. Gör det det, skicka in en DTO utan Credentials*/



            } else {
                tempPassword = employeeDTO.getPassword();
            }

            if (!employeeDTO.getFirstName().equals(optEmp.get().getFirstName()) ||
                    !employeeDTO.getLastName().equals(optEmp.get().getLastName()) ||
                    !employeeDTO.getEmail().equals(optEmp.get().getEmail())) {

                UpdateUserInfoKeycloakDTO updateUserInfoKeycloakDTO = new UpdateUserInfoKeycloakDTO();

                updateUserInfoKeycloakDTO.setFirstName(employeeDTO.getFirstName());
                updateUserInfoKeycloakDTO.setLastName(employeeDTO.getLastName());
                updateUserInfoKeycloakDTO.setEmail(employeeDTO.getEmail());

                String response = keycloakService.updateUserInfo(updateUserInfoKeycloakDTO, optEmp.get().getEmail());
            }

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            if(employeeDTO.getPassword() != null && !employeeDTO.getPassword().isEmpty() && !encoder.matches(employeeDTO.getPassword(), optEmp.get().getPassword())) {
                CredentialsUpdate credentials = new CredentialsUpdate();

                credentials.setType("password");
                credentials.setValue(tempPassword);
                keycloakService.updateUserPassword(credentials, optEmp.get().getEmail());
            }


            optEmp.get().setFirstName(employeeDTO.getFirstName());
            optEmp.get().setLastName(employeeDTO.getLastName());
            optEmp.get().setPassword(tempPassword);
            optEmp.get().setSsNumber(employeeDTO.getSsNumber());
            optEmp.get().setEmail(employeeDTO.getEmail());
            optEmp.get().setPhoneNumber(employeeDTO.getPhoneNumber());
            optEmp.get().setAddress(employeeDTO.getAddress());
            optEmp.get().setRole(employeeDTO.getRole());
            optEmp.get().getSalary().setHourlySalary(employeeDTO.getHourlySalary());
            employeeRepository.save(optEmp.get());
        } else {
            throw new PersonDoesNotExistException("No employee with that id was found!");
        }

        return employeeToGetEmployeeDto(optEmp.get());
    }

    public SalaryDTO getSalary(Long id) {
        Optional<Employee> optEmp = employeeRepository.findById(id);

        SalaryDTO salaryDTO;

        if (optEmp.isPresent()) {
            salaryDTO = new SalaryDTO(
                    optEmp.get().getSalary().getWorkedHours(),
                    optEmp.get().getSalary().getHourlySalary()
            );
        } else {
            salaryDTO = new SalaryDTO();
        }
        return salaryDTO;
    }

    private GetEmployeeDTO employeeToGetEmployeeDto(Employee employee) {
        /** Converts Employee to GetEmployeeDto*/
        return new GetEmployeeDTO(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getSsNumber(),
                employee.getEmail(),
                employee.getPhoneNumber(),
                employee.getAddress(),
                employee.getCity(),
                employee.getPostalCode(),
                employee.getRole(),
                employee.getSalary().getHourlySalary());
    }

    private Boolean checkCreateEmployeeDTO(CreateEmployeeDTO dto) {
        /* Checks so that fields are not null, and that email and phone number are valid formats.
         Returns true if all fields are ok.*/
        return dto.getFirstName() != null
                && dto.getLastName() != null
                && dto.getPassword() != null
                && dto.getSsNumber() != null
                && dto.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
                && dto.getPhoneNumber().matches("^(\\d+){8,12}$")
                && dto.getAddress() != null
                && dto.getRole() != null;
    }

    @Autowired
    private EmailService emailService;

    private Boolean checkEditEmployeeDTO(EditEmployeeDTO dto) {
        /* Checks so that fields are not null, and that email and phone number are valid formats.
          Returns true if all fields are ok.*/
        return dto.getFirstName() != null
                && dto.getLastName() != null
                && dto.getSsNumber() != null
                && dto.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
                && dto.getPhoneNumber().matches("^(\\d+){8,12}$")
                && dto.getAddress() != null
                && dto.getRole() != null
                && dto.getHourlySalary() != 0;
    }

    public EmployeeAuthenticationResponseDTO createEmp(CreateEmployeeDTO createDTO) {

        Optional<Employee> optEmpEmail = employeeRepository.findByEmail(createDTO.getEmail());


        if (optEmpEmail.isPresent()) {
            throw new PersonAlreadyExistsException(createDTO.getEmail());
        }

        if (createDTO.getFirstName() != "") {
            if (!checkCreateEmployeeDTO(createDTO)) {
                throw new InvalidRequestException("Some fields had incorrect or missing information.");
            }
        }

        String keycloakResponse = keycloakService.createUser
                (new CreateUserDTO(
                        createDTO.getEmail(),
                        createDTO.getFirstName(),
                        createDTO.getLastName(),
                        createDTO.getPassword()));
        if (!keycloakResponse.equals("201 CREATED")) {
            throw new HttpRequestFailedException("Failed to create user in keycloak step 1.");
        }

        String adminToken = keycloakService.getAdminToken();

        try {
            keycloakService.assignRoleToUser("employee", createDTO.getEmail(), adminToken);
        } catch (HttpRequestFailedException e) {
            throw new HttpRequestFailedException("Failed to get userId or failed to assign role to user");
        }

        try {

            Salary salary = new Salary(createDTO.getSalary());
            if (!createDTO.getEmail().isEmpty()) {
                Employee emp = new Employee(
                        createDTO.getFirstName(),
                        createDTO.getLastName(),
                        createDTO.getPassword(),
                        createDTO.getSsNumber(),
                        createDTO.getEmail(),
                        createDTO.getPhoneNumber(),
                        createDTO.getAddress(),
                        createDTO.getCity(),
                        createDTO.getPostalCode(),
                        createDTO.getRole(),
                        salary,
                        List.of()
                );

                salary.setEmployee(emp);
                emp.setPassword(createDTO.getPassword());
                employeeRepository.save(emp);

                emailService.sendEmail(createDTO.getEmail(),
                        "StädaFint AB",
                        "You are now a registered employee at StädaFintAB. Welcome!");

                return new EmployeeAuthenticationResponseDTO(keycloakService.getUserToken(
                        createDTO.getEmail(), createDTO.getPassword()).getBody().getAccess_token(),
                        String.valueOf(emp.getId())
                );
            }
        } catch (Exception e) {
            throw new RuntimeException(("An unexpected error occurred."));
        }
        throw new RuntimeException(("An unexpected error occurred."));
    }
}
