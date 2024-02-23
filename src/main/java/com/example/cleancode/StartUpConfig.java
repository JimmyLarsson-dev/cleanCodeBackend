package com.example.cleancode;

import com.example.cleancode.customer.Customer;
import com.example.cleancode.customer.CustomerRepository;
import com.example.cleancode.employees.Employee;
import com.example.cleancode.employees.EmployeeRepository;
import com.example.cleancode.employees.Salary;
import com.example.cleancode.enums.*;
import com.example.cleancode.job.CreateJobDTO;
import com.example.cleancode.job.JobService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.example.cleancode.enums.TimeSlots.*;

@Configuration
public class StartUpConfig {

    private final EmployeeRepository employeeRepository;
    private final CustomerRepository customerRepository;
    private final JobService jobService;

    public StartUpConfig(EmployeeRepository employeeRepository, CustomerRepository customerRepository, JobService jobService) {
        this.employeeRepository = employeeRepository;
        this.customerRepository = customerRepository;
        this.jobService = jobService;
    }

    @Bean
    public CommandLineRunner initCustomerDatabase(CustomerRepository customerRepository) {
        return args -> {
            customerRepository.save(new Customer(
                    UUID.fromString("678b8da4-28cd-42c9-b6c6-a3dc53204632"),
                    "Lars",
                    "Olofsson",
                    "password",
                    "Larssons måleri AB",
                    "123445660654-57439",
                    "lars.olofsson@maleri.se",
                    "0730123123",
                    "Adressgatan 12",
                    "Stockholm",
                    "11356",
                    CustomerType.BUSINESS
            ));

            customerRepository.save(new Customer(

                    UUID.fromString("e7b043d9-0264-429a-8073-c5524e914c53"),
                    "Hanna",
                    "Root",
                    "password",
                    "hanna.root@ikea.se",
                    "0730123123",
                    "Adressgatan 13",
                    "Stadberga",
                    "34512",
                    CustomerType.PRIVATE
            ));

            customerRepository.save(new Customer(
                    UUID.fromString("978b8da4-28cd-42c9-b6c6-a3dc53204632"),
                    "Stina",
                    "Fina",
                    "password",
                    "StadaFintAB@gmail.com",
                    "0700123123",
                    "Dirtyroad 12",
                    "Stockholm",
                    "41356",
                    CustomerType.PRIVATE
            ));
        };
    }

    @Bean
    public CommandLineRunner initEmployeeDatabase(EmployeeRepository employeeRepository) {

        Salary salary1 = new Salary(400);
        Employee employee1 = new Employee(
                "Lisa",
                "Grönberg",
                "password",
                "199111129674",
                "lisa.gronberg@stadafint.se",
                "0702424242",
                "Skogsvägen 65",
                "Köping",
                "54376",
                Role.ADMIN,
                salary1,
                List.of());
        salary1.setEmployee(employee1);

        Salary salary2 = new Salary(180);
        Employee employee2 = new Employee(
                "Kent",
                "Andersson",
                "password",
                "197404119437",
                "kent.andersson@stadafint.se",
                "0704123323",
                "Bergstigen 23",
                "Varberg",
                "98712",
                Role.EMPLOYEE,
                salary2,
                List.of());
        salary2.setEmployee(employee2);

        Salary salary3 = new Salary(150);
        Employee employee3 = new Employee(
                "Klas",
                "McClean",
                "password",
                "199901181373",
                "klas.mcclean@stadafint.se",
                "0702424542",
                "Bollgatan 66",
                "Göteborg",
                "43209",
                Role.EMPLOYEE,
                salary3,
                List.of());
        salary3.setEmployee(employee3);

        Salary salary4 = new Salary(150);
        Employee employee4 = new Employee(
                "Sten",
                "Sturesson",
                "password",
                "197304165789",
                "sten.sturesson@stadafint.se",
                "0708947468",
                "Sne allén 4",
                "Karlstad",
                "64783",
                Role.EMPLOYEE,
                salary4,
                List.of());
        salary4.setEmployee(employee4);

        Salary salary5 = new Salary(200);
        Employee employee5 = new Employee(
                "Johanna",
                "Qvist",
                "password",
                "199712180762",
                "johanna.qvist@stadafint.se",
                "0706788263",
                "Hagadalsvägen 3",
                "Växjö",
                "32465",
                Role.EMPLOYEE,
                salary5,
                List.of());
        salary5.setEmployee(employee5);

        Salary salary6 = new Salary(175);
        Employee employee6 = new Employee(
                "Yelena",
                "Petkovic",
                "password",
                "198208258638",
                "yelena.petkovic@stadafint.se",
                "0705803746",
                "Kalasvägen 85 N",
                "Lidköping",
                "49384",
                Role.EMPLOYEE,
                salary6,
                List.of());
        salary6.setEmployee(employee6);

        Salary salary7 = new Salary(150);
        Employee employee7 = new Employee(
                "Love",
                "Putter",
                "password",
                "200307193791",
                "love.putter@stadafint.se",
                "0700347840",
                "Hejvägen 67",
                "Kiruna",
                "83462",
                Role.EMPLOYEE,
                salary7,
                List.of());
        salary7.setEmployee(employee7);

        Salary salary8 = new Salary(210);
        Employee employee8 = new Employee(
                "Kim",
                "Hellgren",
                "password",
                "198801153626",
                "kim.hellgren@stadafint.se",
                "0701643809",
                "Gladafintvägen 5",
                "Jönköping",
                "48096",
                Role.EMPLOYEE,
                salary8,
                List.of());
        salary8.setEmployee(employee8);

        Salary salary9 = new Salary(160);
        Employee employee9 = new Employee(
                "Kevin",
                "Ström",
                "password",
                "199508288279",
                "kevin.strom@stadafint.se",
                "0704526058",
                "Fulstigen 92",
                "Stockholm",
                "14267",
                Role.EMPLOYEE,
                salary9,
                List.of());
        salary9.setEmployee(employee9);

        Salary salary10 = new Salary(190);
        Employee employee10 = new Employee(
                "Parisa",
                "Hardashian",
                "password",
                "199109110596",
                "parisa.hardashian@stadafint.se",
                "0701380263",
                "Gladahäng gränden 35",
                "Malmö",
                "26946",
                Role.EMPLOYEE,
                salary10,
                List.of());
        salary10.setEmployee(employee10);

        Salary salary11 = new Salary(170);
        Employee employee11 = new Employee(
                "Carl",
                "Johansson",
                "password",
                "198104093781",
                "carl.johansson@stadafint.se",
                "0709262738",
                "Harmonigatan 56",
                "Orsa",
                "61823",
                Role.EMPLOYEE,
                salary11,
                List.of());
        salary11.setEmployee(employee11);

        Salary salary12 = new Salary(150);
        Employee employee12 = new Employee(
                "Emma",
                "Ölund",
                "password",
                "198906287342",
                "emma.olund@stadafint.se",
                "0709436914",
                "Paradvägen 67 A",
                "Luleå",
                "57361",
                Role.EMPLOYEE,
                salary12,
                List.of());
        salary12.setEmployee(employee12);

        Salary salary13 = new Salary(190);
        Employee employee13 = new Employee(
                "Ragnar",
                "Smith",
                "password",
                "196109018499",
                "ragnar.smith@stadafint.se",
                "0701632309",
                "Per Ingvars väg 26",
                "Trelleborg",
                "27484",
                Role.EMPLOYEE,
                salary13,
                List.of());
        salary13.setEmployee(employee13);

        Salary salary14 = new Salary(175);
        Employee employee14 = new Employee(
                "Jusef",
                "Mohammad",
                "password",
                "199310199473",
                "jusef.mohammad@stadafint.se",
                "0702745912",
                "Gågatan 29",
                "Stockholm",
                "12846",
                Role.EMPLOYEE,
                salary14,
                List.of());
        salary14.setEmployee(employee14);

        Salary salary15 = new Salary(160);
        Employee employee15 = new Employee(
                "Linnea",
                "Fridolfsson",
                "password",
                "200009216548",
                "linnea.fridolfsson@stadafint.se",
                "0703900443",
                "Hallon gränden 1",
                "Västervik",
                "39475",
                Role.EMPLOYEE,
                salary15,
                List.of());
        salary15.setEmployee(employee15);

        Salary salary16 = new Salary(200);
        Employee employee16 = new Employee(
                "Emil",
                "Krufs",
                "password",
                "199603148275",
                "emil.krufs@stadafint.se",
                "0702736845",
                "Kanonvägen 38 C",
                "Uppsala",
                "19053",
                Role.EMPLOYEE,
                salary16,
                List.of());
        salary16.setEmployee(employee16);

        Salary salary17 = new Salary(170);
        Employee employee17 = new Employee(
                "Mikael",
                "Andersen",
                "password",
                "197706130689",
                "mikael.andersen@stadafint.se",
                "0709032256",
                "Klokvägen 67",
                "Skellefteå",
                "57356",
                Role.EMPLOYEE,
                salary17,
                List.of());
        salary17.setEmployee(employee17);

        Salary salary18 = new Salary(150);
        Employee employee18 = new Employee(
                "Elvira",
                "Melusic",
                "password",
                "200112228492",
                "elvira.melusic@stadafint.se",
                "0700173829",
                "Bråkmakargatan 23",
                "Vimmerby",
                "35731",
                Role.EMPLOYEE,
                salary18,
                List.of());
        salary18.setEmployee(employee18);

        Salary salary19 = new Salary(180);
        Employee employee19 = new Employee(
                "Steffe",
                "Sundkvist",
                "password",
                "196507219401",
                "steffe.sundkvist@stadafint.se",
                "0702611628",
                "Västmannavägen 90",
                "Sölvesborg",
                "47384",
                Role.EMPLOYEE,
                salary19,
                List.of());
        salary19.setEmployee(employee19);

        return args -> {
            employeeRepository.save(employee1);
            employeeRepository.save(employee2);
            employeeRepository.save(employee3);
//            employeeRepository.save(employee4);
//            employeeRepository.save(employee5);
//            employeeRepository.save(employee6);
//            employeeRepository.save(employee7);
//            employeeRepository.save(employee8);
//            employeeRepository.save(employee9);
//            employeeRepository.save(employee10);
//            employeeRepository.save(employee11);
//            employeeRepository.save(employee12);
//            employeeRepository.save(employee13);
//            employeeRepository.save(employee14);
//            employeeRepository.save(employee15);
//            employeeRepository.save(employee16);
//            employeeRepository.save(employee17);
//            employeeRepository.save(employee18);
//            employeeRepository.save(employee19);
        };
    }

    @Bean
    public CommandLineRunner initJobDatabase(JobService jobService) {
        return args -> {
            List<TimeSlots> timesList = new ArrayList<>();
            LocalDateTime dateInDateFormat = LocalDateTime.parse("2023-11-24T12:00");

            String dateInString5 = "2024-05-10";
            LocalDate localDate5 = LocalDate.parse(dateInString5);
            Date date5 = Date.from(localDate5.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());


            timesList.add(EIGHT);
            jobService.createJob(new CreateJobDTO(
                    Jobtype.BASIC,
                    date5,
                    List.of(EIGHT),
                    55,
                    PaymentOption.KLARNA,
                    UUID.fromString("678b8da4-28cd-42c9-b6c6-a3dc53204632"),
                    "Städa fint tack."));

            timesList.clear();
            timesList.add(NINE);
            jobService.createJob(new CreateJobDTO(
                    Jobtype.BASIC,
                    date5,
                    List.of(NINE),
                    55,
                    PaymentOption.KLARNA,
                    UUID.fromString("678b8da4-28cd-42c9-b6c6-a3dc53204632"),
                    "Städa gärna under kylen"));

            timesList.clear();
            timesList.add(TEN);
            jobService.createJob(new CreateJobDTO(
                    Jobtype.BASIC,
                    date5,
                    List.of(TEN),
                    50,
                    PaymentOption.KLARNA,
                    UUID.fromString("e7b043d9-0264-429a-8073-c5524e914c53"),
                    "Damma lamporna"));

            String dateInString = "2023-11-24";
            LocalDate localDate = LocalDate.parse(dateInString);
            Date date2 = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

            jobService.createJob(new CreateJobDTO(
                    Jobtype.BASIC,
                    date2,
                    List.of(THIRTEEN),
                    99,
                    PaymentOption.KLARNA,
                    UUID.fromString("678b8da4-28cd-42c9-b6c6-a3dc53204632"),
                    "Kan ni raka min katt?"));

            jobService.createJob(new CreateJobDTO(
                    Jobtype.ADVANCED,
                    date2,
                    List.of(FOURTEEN),
                    30,
                    PaymentOption.KLARNA,
                    UUID.fromString("e7b043d9-0264-429a-8073-c5524e914c53"),
                    "Jag har förskräckligt mycket tandsten, hjälp mig snälla Städafint AB"));

            String dateInString7 = "2024-03-24";
            LocalDate localDate7 = LocalDate.parse(dateInString7);
            Date date7 = Date.from(localDate7.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());


            jobService.createJob(new CreateJobDTO(
                    Jobtype.DIAMOND,
                    date7,
                    List.of(EIGHT),
                    43,
                    PaymentOption.KLARNA,
                    UUID.fromString("e7b043d9-0264-429a-8073-c5524e914c53"),
                    null));

            jobService.createJob(new CreateJobDTO(
                    Jobtype.ADVANCED,
                    date7,
                    List.of(TWELVE),
                    56,
                    PaymentOption.KLARNA,
                    UUID.fromString("678b8da4-28cd-42c9-b6c6-a3dc53204632"),
                    ""));
            jobService.createJob(new CreateJobDTO(
                    Jobtype.BASIC,
                    date7,
                    List.of(SIXTEEN),
                    25,
                    PaymentOption.CASH,
                    UUID.fromString("e7b043d9-0264-429a-8073-c5524e914c53"),
                    ""));
        };
    }
}
