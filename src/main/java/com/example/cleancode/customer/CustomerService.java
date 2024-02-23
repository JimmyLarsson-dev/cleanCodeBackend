package com.example.cleancode.customer;

import com.example.cleancode.authentication.KeycloakService;
import com.example.cleancode.authentication.dto.CreateUserDTO;
import com.example.cleancode.authentication.dto.CredentialsUpdate;
import com.example.cleancode.authentication.dto.UpdateUserInfoKeycloakDTO;
import com.example.cleancode.enums.CustomerType;
import com.example.cleancode.exceptions.*;
import com.example.cleancode.mail.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final KeycloakService keycloakService;


    public CustomerDTO customerEntityToDTO(Customer customer) {

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customer.getId());
        customerDTO.setFirstName(customer.getFirstName());
        customerDTO.setLastName(customer.getLastName());
        customerDTO.setCompanyName(customer.getCompanyName());
        customerDTO.setOrgNumber(customer.getOrgNumber());
        customerDTO.setEmail(customer.getEmail());
        customerDTO.setPhoneNumber(customer.getPhoneNumber());
        customerDTO.setAddress(customer.getAddress());
        customerDTO.setCity(customer.getCity());
        customerDTO.setPostalCode(customer.getPostalCode());
        customerDTO.setCustomerType(customer.getCustomerType());

        return customerDTO;
    }

    @Autowired
    private EmailService emailService;

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository, KeycloakService keycloakService) {
        this.customerRepository = customerRepository;
        this.keycloakService = keycloakService;
    }

    public CustomerAuthenticationResponseDTO createCustomer(CreateCustomerDTO createDTO) {

        Optional<Customer> optCustEmail = customerRepository.findByEmail(createDTO.getEmail());
        //om det är en business så får vi ett värde i company fältet, annars null

        if (optCustEmail.isPresent()) {
            throw new CustomerAlreadyExistsException(createDTO.getEmail());
        }

        if (createDTO.getFirstName() != "") {
            if (!checkCreateCustomerDTO(createDTO)) {
                throw new InvalidRequestException("Some fields had incorrect or missing information.");
            }
        }



        String keycloakResponse = keycloakService.createUser(new CreateUserDTO(createDTO.getEmail(), createDTO.getFirstName(), createDTO.getLastName(), createDTO.getPassword()));
        if (!keycloakResponse.equals("201 CREATED")) {
            throw new HttpRequestFailedException("Failed to create user in keycloak step 1.");
        }

        String adminToken = keycloakService.getAdminToken();

        try {
            keycloakService.assignRoleToUser("customer", createDTO.getEmail(), adminToken);
        } catch (HttpRequestFailedException e) {
            throw new HttpRequestFailedException("Failed to get userId or failed to assign role to user");
        }

        try {
            if (createDTO.getCompanyName().isEmpty() && createDTO.getOrgNumber().isEmpty()) {
                Customer customer = new Customer(
                        UUID.randomUUID(),
                        createDTO.getFirstName(),
                        createDTO.getLastName(),
                        createDTO.getPassword(),
                        createDTO.getEmail(),
                        createDTO.getPhoneNumber(),
                        createDTO.getAddress(),
                        createDTO.getCity(),
                        createDTO.getPostalCode(),
                        CustomerType.PRIVATE);

                customerRepository.save(customer);

                emailService.sendEmail(createDTO.getEmail(),
                        "StädaFint AB",
                        "You are now a registered member at StädaFintAB. Welcome!");

                return new CustomerAuthenticationResponseDTO(keycloakService.getUserToken(createDTO.getEmail(), createDTO.getPassword()), customer.getId().toString());
            }

            if (!createDTO.getCompanyName().isEmpty() && !createDTO.getOrgNumber().isEmpty()) {
                Customer customer = new Customer(
                        UUID.randomUUID(),
                        createDTO.getFirstName(),
                        createDTO.getLastName(),
                        createDTO.getPassword(),
                        createDTO.getCompanyName(),
                        createDTO.getOrgNumber(),
                        createDTO.getEmail(),
                        createDTO.getPhoneNumber(),
                        createDTO.getAddress(),
                        createDTO.getCity(),
                        createDTO.getPostalCode(),
                        CustomerType.BUSINESS);
                customer.setPassword(createDTO.getPassword());
                customerRepository.save(customer);

                emailService.sendEmail(createDTO.getEmail(),
                        "StädaFint AB",
                        "Du är nu registrerad som medlem på StädaFint AB. Välkommen!");
                return new CustomerAuthenticationResponseDTO(keycloakService.getUserToken(createDTO.getEmail(), createDTO.getPassword()), customer.getId().toString());
            }

            // andra delen av "if condition" är överflödig, eftersom annars hamnar vi i någon av return ovan, men det är tydligare att behålla såhär
            if (!createDTO.getCompanyName().isEmpty() && createDTO.getOrgNumber().isEmpty()) {
                throw new CustomerInfoMissMatchException("If companyName isn't null, you need a orgNumber");
            }

            throw new CustomerInfoMissMatchException("if orgNumber isn't null, you need a company name");

//             ^ dom klagar i if-satserna, men tycker dom ser nice ut...
        } catch (Exception e) {
            throw new RuntimeException("ERROR -->" + e.getMessage());
        }
    }

    public String deleteCustomer(UUID id) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);

        if (optionalCustomer.isPresent()) {

            String adminToken = keycloakService.getAdminToken();
            String userId = keycloakService.getUserId(optionalCustomer.get().getEmail(), adminToken);

            String deleteResponse = keycloakService.deleteUser(userId, adminToken);

            if (deleteResponse.contains("204")) {

                optionalCustomer.get().setFirstName("");
                optionalCustomer.get().setLastName("");
                optionalCustomer.get().setPassword("");
                optionalCustomer.get().setCompanyName("");
                optionalCustomer.get().setOrgNumber("");
                optionalCustomer.get().setEmail("");
                optionalCustomer.get().setPhoneNumber("");
                optionalCustomer.get().setAddress("");
                optionalCustomer.get().setCity("");
                optionalCustomer.get().setPostalCode("");

                customerRepository.save(optionalCustomer.get());

                return "Customer with the ID: " + id + " have been removed";
            }

            return "Not deleted";


        } else {
            throw new CustomerDoesNotExistException(id);
        }
    }

    @Transactional
    public CustomerDTO updateCustomerInfo(UUID id, EditCustomerDTO editCustomerDTO) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);

        UpdateUserInfoKeycloakDTO updateUserInfoKeycloakDTO = new UpdateUserInfoKeycloakDTO();

        CredentialsUpdate credentials = new CredentialsUpdate();

        int counter = 0;

        if (optionalCustomer.isPresent()) {
            Customer customerUpdate = optionalCustomer.get();

            String originalEmail = customerUpdate.getEmail();

            if (editCustomerDTO.getFirstName() != null && !editCustomerDTO.getFirstName().equals(customerUpdate.getFirstName())) {
                customerUpdate.setFirstName(editCustomerDTO.getFirstName());
                updateUserInfoKeycloakDTO.setFirstName(editCustomerDTO.getFirstName());
                counter++;
            } else {
                updateUserInfoKeycloakDTO.setFirstName(customerUpdate.getFirstName());
            }

            if (editCustomerDTO.getLastName() != null && !editCustomerDTO.getLastName().equals(customerUpdate.getLastName())) {
                customerUpdate.setLastName(editCustomerDTO.getLastName());
                updateUserInfoKeycloakDTO.setLastName(editCustomerDTO.getLastName());
                counter++;
            } else {
                updateUserInfoKeycloakDTO.setLastName(customerUpdate.getLastName());
            }

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();


            if (!editCustomerDTO.getPassword().isEmpty() && !encoder.matches(editCustomerDTO.getPassword(), optionalCustomer.get().getPassword())) {
                customerUpdate.setPassword(editCustomerDTO.getPassword());

                credentials.setType("password");
                credentials.setValue(editCustomerDTO.getPassword());

                keycloakService.updateUserPassword(credentials, originalEmail);


            }


            if (editCustomerDTO.getCompanyName() != null && customerUpdate.getCustomerType().equals(CustomerType.BUSINESS)) {
                customerUpdate.setCompanyName(editCustomerDTO.getCompanyName());
            }
            if (editCustomerDTO.getOrgNumber() != null && customerUpdate.getCustomerType().equals(CustomerType.BUSINESS)) {
                customerUpdate.setOrgNumber(editCustomerDTO.getOrgNumber());
            }
            if (!editCustomerDTO.getEmail().isEmpty() && !editCustomerDTO.getEmail().equals(customerUpdate.getEmail())) {

                customerUpdate.setEmail(editCustomerDTO.getEmail());
                updateUserInfoKeycloakDTO.setEmail(editCustomerDTO.getEmail());
                counter++;
            } else {
                updateUserInfoKeycloakDTO.setEmail(customerUpdate.getEmail());

            }

            if (editCustomerDTO.getPhoneNumber() != null) {
                customerUpdate.setPhoneNumber(editCustomerDTO.getPhoneNumber());
            }
            if (editCustomerDTO.getAddress() != null) {
                customerUpdate.setAddress(editCustomerDTO.getAddress());
            }
            if (editCustomerDTO.getCity() != null) {
                customerUpdate.setCity(editCustomerDTO.getCity());
            }
            if (editCustomerDTO.getPostalCode() != null) {
                customerUpdate.setPostalCode(editCustomerDTO.getPostalCode());
            }
            if (editCustomerDTO.getCustomerType() != null) {
                customerUpdate.setCustomerType(editCustomerDTO.getCustomerType());
            }

            customerRepository.save(customerUpdate);

            if (counter > 0) {
            keycloakService.updateUserInfo(updateUserInfoKeycloakDTO, originalEmail);

            }


            return customerEntityToDTO(customerUpdate);
        } else {
            throw new CustomerDoesNotExistException(id);
        }
    }


    /**
     * Spara det här så länge
     */
//    @Transactional
//    public CustomerDTO updateCustomerInfoo(UUID id, CustomerDTO customerDTO) {
//        Customer customerUpdate = customerRepository.findById(id)
//                .orElseThrow(() -> new CustomerDoesNotExistException(id));
//
//        Map<Function<CustomerDTO, ?>, Consumer<Object>> updateActions = new HashMap<>();
//        updateActions.put(CustomerDTO::getFirstName, customerUpdate::setFirstName);
//        updateActions.put(CustomerDTO::getLastName, customerUpdate::setLastName);
//        updateActions.put(dto -> dto.getCustomerType() == CustomerType.BUSINESS, dto -> {
//            updateActions.put(CustomerDTO::getCompanyName, customerUpdate::setCompanyName);
//            updateActions.put(CustomerDTO::getOrgNumber, customerUpdate::setOrgNumber);
//        });
//        updateActions.put(CustomerDTO::getEmail, customerUpdate::setEmail);
//        updateActions.put(CustomerDTO::getPhoneNumber, customerUpdate::setPhoneNumber);
//        updateActions.put(CustomerDTO::getAddress, customerUpdate::setAddress);
//        updateActions.put(CustomerDTO::getCity, customerUpdate::setCity);
//        updateActions.put(CustomerDTO::getPostalCode, customerUpdate::setPostalCode);
//        updateActions.put(dto -> dto.getCustomerType() != null, dto -> customerUpdate.setCustomerType(dto.getCustomerType()));
//
//        updateActions.forEach((condition, action) -> {
//            if (condition.apply(customerDTO)) {
//                action.accept(customerDTO);
//            }
//        });
//
//        customerRepository.save(customerUpdate);
//
//        return customerEntityToDTO(customerUpdate);
//    }
    public List<CustomerDTO> getAllCustomers() {
        List<Customer> allCustomers = customerRepository.findAll();

        List<Customer> filteredList = allCustomers
                .stream()
                .filter(x -> !x.getEmail().isEmpty())
                .toList();

        List<CustomerDTO> allCustomersDTO = new ArrayList<>();
        for (Customer customer : filteredList) {
            allCustomersDTO.add(customerEntityToDTO(customer));
        }
        return allCustomersDTO;
    }

    public CustomerDTO getCustomerById(UUID id) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if (optionalCustomer.isPresent()) {
            return customerEntityToDTO(optionalCustomer.get());
        } else {
            throw new CustomerDoesNotExistException(id);
        }
    }

    public UUID getCustomerByEmail(String email) {
        UUID optEmail = customerRepository.findByEmail(email).get().getId();

        if (optEmail != null) {
            return optEmail;
        } else {
            throw new CustomerWithEmailDoesNotExistException(email);
        }
    }

    private boolean checkCreateCustomerDTO(CreateCustomerDTO createCustomerDTO) {
        return createCustomerDTO.getFirstName() != null
                && createCustomerDTO.getLastName() != null
                && (createCustomerDTO.getPassword() != null && createCustomerDTO.getPassword().length() >= 8)
                && createCustomerDTO.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
                && createCustomerDTO.getPhoneNumber().matches("^(\\d+){8,12}$")
                && createCustomerDTO.getAddress() != null;
    }
}
