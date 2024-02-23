package com.example.cleancode.job;

import com.example.cleancode.booked.Booked;
import com.example.cleancode.booked.BookedRepository;
import com.example.cleancode.customer.Customer;
import com.example.cleancode.customer.CustomerRepository;
import com.example.cleancode.employees.Employee;
import com.example.cleancode.employees.EmployeeRepository;
import com.example.cleancode.enums.JobStatus;
import com.example.cleancode.enums.Role;
import com.example.cleancode.enums.TimeSlots;
import com.example.cleancode.exceptions.*;
import com.example.cleancode.mail.EmailService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;
import static com.example.cleancode.enums.Jobtype.*;
import static com.example.cleancode.enums.Role.EMPLOYEE;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final EmployeeRepository employeeRepository;
    private final CustomerRepository customerRepository;
    private final BookedRepository bookedRepository;
    private final EmailService emailService;

    public JobService(
            JobRepository jobRepository,
            EmployeeRepository employeeRepository,
            CustomerRepository customerRepository,
            BookedRepository bookedRepository,
            EmailService emailService) {
        this.jobRepository = jobRepository;
        this.employeeRepository = employeeRepository;
        this.customerRepository = customerRepository;
        this.bookedRepository = bookedRepository;
        this.emailService = emailService;
    }


    public List<Employee> findUnbookedEmployees(Date date, List<TimeSlots> timeSlot) {

        List<Employee> employees = employeeRepository.findUnbookedEmployees(date, timeSlot.get(0));

        return employees.stream().filter(x -> x.getRole().equals(Role.EMPLOYEE)).toList();
    }

    public List<Boolean> getAvailableEmployees(Date date, int lookForAvailableThisManyHours) {

        List<List<Employee>> employeeListList = new ArrayList<>();
        List<Employee> empList;
        List<TimeSlots> timeSlotList = List.of(
                TimeSlots.EIGHT,
                TimeSlots.NINE,
                TimeSlots.TEN,
                TimeSlots.ELEVEN,
                TimeSlots.TWELVE,
                TimeSlots.THIRTEEN,
                TimeSlots.FOURTEEN,
                TimeSlots.FIFTEEN,
                TimeSlots.SIXTEEN
        );
        for (int i = 0; i < timeSlotList.size(); i++) {

            empList = employeeRepository.findUnbookedEmployees(date, timeSlotList.get(i));

            employeeListList.add(empList
                    .stream()
                    .filter(x -> x.getRole().equals(EMPLOYEE))
                    .collect(Collectors.toList()));
        }

        employeeListList.add(List.of());
        employeeListList.add(List.of());

        List<Boolean> boolList = new ArrayList<>(List.of(false, false, false, false, false, false, false, false, false));

        for (int i = 0; i < employeeListList.size(); i++) {

       //  Loopar igenom yttersta listan, som innehåller listor med tillgängliga employees ett visst klockslag och datum

            for (int j = 0; j < employeeListList.get(i).size(); j++) {

                 //Loopar igenom inre listorna, som innehåller tillgängliga employees ett visst klockslag och datum
                for (int k = 0; k < lookForAvailableThisManyHours; k++) {

                     //loopar lookForAvailableThisManyHours gånger, kollar så många listor framåt

                    if (employeeListList.get(i + k).contains(employeeListList.get(i).get(j))) {
                        boolList.set(i, true);
                    }
                }
            }
        }
        if (lookForAvailableThisManyHours == 2) {
            boolList.set(8, false);
        }
        if (lookForAvailableThisManyHours == 3) {
            boolList.set(8, false);
            boolList.set(7, false);
        }
        return boolList;
    }

    public Long createJob(CreateJobDTO createJobDTO) {

        Date date = createJobDTO.getDate();
        TimeSlots timeSlot = createJobDTO.getTimeSlotList().get(0);
        List<Employee> unbookedEmployees = findUnbookedEmployees(date, createJobDTO.getTimeSlotList());
        Optional<Customer> customerOptional = customerRepository.findById(createJobDTO.getCustomerId());

        if (customerOptional.isEmpty()) {
            throw new CustomerDoesNotExistException(createJobDTO.getCustomerId());
        }
        if (unbookedEmployees.isEmpty()) {
            throw new RuntimeException("No employees are available for this time slot.");
        }
        /** Assign random employee from the unbookedEmployees list */
        Employee assignedEmployee = assignEmployeeToJob(unbookedEmployees);

        Job job = new Job(
                createJobDTO.getJobtype(),
                date,
                timeSlot,
                JobStatus.PENDING,
                createJobDTO.getSquareMeters(),
                createJobDTO.getPaymentOption(),
                assignedEmployee,
                customerOptional.get(),
                createJobDTO.getMessage(),
                0);
        jobRepository.save(job);
        updateAvailability(assignedEmployee, date, createJobDTO.getTimeSlotList(), job);

        String body = "Your booking has been approved and logged into our system!";

        emailService.sendEmail(customerOptional.get().getEmail(),
                "StädaFint AB",
                body);

        return job.getJobId();
    }

    public void updateAvailability(Employee selectedEmployee, Date date, List<TimeSlots> timeSlotList, Job job) {
        /* Denna metod anropas från createJob. Den lägger till rader i BookedRepository med de uppbokade tiderna, så
         att vi inte kan boka fler städningar än vi har städare. */

        for (int i = 0; i < timeSlotList.size(); i++) {

            Optional<Booked> optionalAvailability = bookedRepository.findByDateAndTimeSlots(date, timeSlotList.get(i));
            Booked booked;

            if (optionalAvailability.isPresent()) {
                booked = optionalAvailability.get();
            } else {
                booked = new Booked();
                booked.setDate(date);
                booked.setTimeSlots(timeSlotList.get(i));
            }
            booked.addEmployee(selectedEmployee);

            booked.getJobs().add(job);
            job.getBooked().add(booked);

            bookedRepository.save(booked);
        }
    }

    private Employee assignEmployeeToJob(List<Employee> unbookedEmployees) {
        int randomAvailableEmployee = (int) Math.floor(Math.random() * unbookedEmployees.size());
        Employee assignedEmployee = unbookedEmployees.get(randomAvailableEmployee);
        return assignedEmployee;
    }

    public Long deleteJob(Long id) {
        Optional<Job> optJob = jobRepository.findById(id);
        if (optJob.isPresent()) {
            jobRepository.deleteById(id);
            return id;
        } else {
            throw new PersonDoesNotExistException("There is no job with that id in database.");
        }
    }

    public GetJobForUpdate getJob(Long id) {
        Optional<Job> optJob = jobRepository.findById(id);

        if (optJob.isPresent()) {
            Job job = optJob.get();
            UUID customerId = job.getCustomer().getId(); // Assuming getCustomerId exists in Customer
            return new GetJobForUpdate(
                    job.getJobId(),
                    job.getJobtype(),
                    job.getDate(),
                    job.getTimeSlot(),
                    job.getJobStatus(),
                    job.getSquareMeters(),
                    job.getPaymentOption(),
                    job.getMessage(),
                    customerId
            );
        } else {
            throw new JobDoesNotExistException("There is no job with that id in the database.");
        }
    }

    public List<GetJobDTO> getAllJobs() {
        List<Job> allJobs = jobRepository.findAll();
        return convertToDTOList(allJobs);
    }

    private List<GetJobDTO> convertToDTOList(List<Job> jobs) {
        return jobs.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private GetJobDTO convertToDTO(Job job) {
        return new GetJobDTO(
                job.getJobId(),
                job.getJobtype(),
                job.getDate().toString(),
                job.getTimeSlot(),
                job.getJobStatus(),
                job.getSquareMeters(),
                job.getPaymentOption(),
                job.getMessage(),
                job.getCustomer(),
                job.getRating()
        );
    }

    public List<GetJobDTO> getAllJobsForEmployee(Long empId) {

        List<Job> jobsForEmployee = jobRepository.findAll()
                .stream()
                .filter(job -> job.getEmployee().getId() == empId)
                .collect(Collectors.toList());
        if (!jobsForEmployee.isEmpty()) {
            return convertToDTOList(jobsForEmployee);
        } else {
            //returnerar tom lista här istället för throw, annars blir det fel när vi försöker mappa i frontend.
            return new ArrayList<>();
        }
    }

    public List<GetJobDTO> getAllJobsForCustomer(UUID cusId) {
        List<Job> jobsForCustomer = jobRepository.findAll()
                .stream()
                .filter(job -> job.getCustomer().getId().equals(cusId))
                .collect(Collectors.toList());

        if (!jobsForCustomer.isEmpty()) {
            return convertToDTOList(jobsForCustomer);
        } else {
            throw new NoJobsForCustomerException("There are no jobs attached to this customer");
        }
    }

    @Transactional
    public GetJobDTO updateJobInfo(UpdateJobDTO jobDTO) {
        Optional<Job> optionalJob = jobRepository.findById(jobDTO.getJobId());

        if (optionalJob.isPresent()) {
            Job jobToUpdate = optionalJob.get();

            // Update properties from the dto
            if (jobDTO.getJobtype() != null && !jobDTO.getJobtype().equals(optionalJob.get().getJobtype())) {
                /* Om jobbtyp har ändrats behöver vi boka om
                  Vi kollar så att det finns lediga timeslots på det efterfrågade datumet */
                Optional<Job> currentJob = jobRepository.findById(jobDTO.getJobId());
                jobRepository.deleteById(jobDTO.getJobId());
                //Date.from(Instant.from(LocalDateTime.parse(jobDTO.getDate())));
                List<Employee> availableEmployees = findUnbookedEmployees(jobDTO.getDate(), jobDTO.getTimeSlotsList());
                if (!availableEmployees.isEmpty()) {
                    jobToUpdate.setJobtype(jobDTO.getJobtype());

                } else {
                    jobRepository.save(currentJob.get());
                    throw new InvalidRequestException("That booking can't be updated, since we do not have available employees at that time");
                }
            }


            if (jobDTO.getJobStatus() != null && !jobDTO.getJobStatus().equals(jobToUpdate.getJobStatus())) {

                jobToUpdate = handleUpdatedJobStatus(jobDTO, jobToUpdate);

            }
            if (jobDTO.getPaymentOption() != null) {
                jobToUpdate.setPaymentOption(jobDTO.getPaymentOption());
            }

            if(jobDTO.getSquareMeters() != 0 && jobDTO.getSquareMeters() != jobToUpdate.getSquareMeters()) {
                jobToUpdate.setSquareMeters(jobDTO.getSquareMeters());
            }
            if (jobDTO.getMessage() != null){
                if(!jobDTO.getMessage().equals(jobToUpdate.getMessage())){
                    jobToUpdate.setMessage(jobDTO.getMessage());
                }
            }

            if(jobDTO.getRating() != 0){
                jobToUpdate.setRating(jobDTO.getRating());
            }

            jobRepository.save(jobToUpdate);

            // return  updated job as DTO
            return convertToDTO(jobToUpdate);
        } else {
            throw new JobDoesNotExistException("Job does not exist");
        }
    }

    public List<Job> getJobsByStatus(List<JobStatus> statuses) {
        return jobRepository.findByJobStatusIn(statuses);
    }


    private Job handleUpdatedJobStatus(UpdateJobDTO updateJobDTO, Job jobToUpdate) {

        Optional<Customer> thisCustomer = customerRepository.findById(updateJobDTO.getCustomerId());

        switch (updateJobDTO.getJobStatus()) {
            case PENDING: {

                emailService.sendEmail(
                        thisCustomer.get().getEmail(),
                        "Ny bokning hos StädaFint!",
                        "Du har en ny bokning hos StädaFint AB! \nVåra duktiga städare kommer till dig " + updateJobDTO.getDate() + ".");
                jobToUpdate.setJobStatus(updateJobDTO.getJobStatus());
                return jobToUpdate;

            }
            case DONE: {
                Optional<Employee> emp = employeeRepository.findById(jobToUpdate.getEmployee().getId());
                int hours = 3;
                if (jobToUpdate.getJobtype().equals(BASIC) || jobToUpdate.getJobtype().equals(WINDOW)) {
                    hours = 1;
                } else if (jobToUpdate.getJobtype().equals(ADVANCED)) {
                    hours = 2;
                }

                if (emp.isPresent()) {
                    emp.get().getSalary().setWorkedHours(emp.get().getSalary().getWorkedHours() + hours);
                }

               emailService.sendEmail(
                        thisCustomer.get().getEmail(),
                        "Din städning har utförts hos StädaFint AB!",
                        "Din städning har utförts! Gå in på Mina Sidor för att godkänna städningen och komma vidare till betalning.");
                jobToUpdate.setJobStatus(updateJobDTO.getJobStatus());
                return jobToUpdate;

            }
            case APPROVED: {
                emailService.sendEmail(
                        thisCustomer.get().getEmail(),
                        "Nytt status på din bokning hos StädaFint!",
                        "Din bokning är nu godkänt och reda att betalas hos StädaFint AB!");

                jobToUpdate.setJobStatus(updateJobDTO.getJobStatus());
                return jobToUpdate;
            }
            case UNAPPROVED: {
                jobToUpdate.setJobStatus(updateJobDTO.getJobStatus());
                return jobToUpdate;
            }
            case PROCESSING: {
                jobToUpdate.setJobStatus(updateJobDTO.getJobStatus());
                return jobToUpdate;
            }
            case PAID: {
                emailService.sendEmail(
                        thisCustomer.get().getEmail(),
                        "Tack för betalning!",
                        "Din betalning av ett städ har genomförts. Tack för att du valde StädaFint AB!");
                jobToUpdate.setJobStatus(updateJobDTO.getJobStatus());
                return jobToUpdate;
            }

            case CANCELLED: {
                String message = updateJobDTO.getMessage();

                if (updateJobDTO.getMessage() == null || updateJobDTO.getMessage().isEmpty()) {
                    message = jobToUpdate.getMessage();
                }
                jobToUpdate.setJobStatus(JobStatus.CANCELLED);
                jobToUpdate.setMessage(message);
                jobToUpdate.getBooked().clear();

                return jobToUpdate;
            }
            default: {
                return jobToUpdate;
            }
        }
    }

    public List<GetJobDTO> getAllJobsForCustomerWithStatus(UUID cusId, List<JobStatus> status) {
        List<Job> jobsForCustomer = jobRepository.findAll()
                .stream()
                .filter(job -> job.getCustomer().getId().equals(cusId) && status.contains(job.getJobStatus()))
                .collect(Collectors.toList());

        if (!jobsForCustomer.isEmpty()) {
            return convertToDTOList(jobsForCustomer);
        } else {
            return new ArrayList<>();
        }
    }
    public List<GetJobDTO> getAllJobsForEmployeeWithStatus(Long empId, List<JobStatus> status) {
        List<Job> jobsForEmployee = jobRepository.findAll()
                .stream()
                .filter(job -> job.getEmployee().getId() == empId && status.contains(job.getJobStatus()))
                .collect(Collectors.toList());

        if (!jobsForEmployee.isEmpty()) {
            return convertToDTOList(jobsForEmployee);
        } else {
            return new ArrayList<>();
        }
    }
}
