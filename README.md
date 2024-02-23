<h2>CleanCode - a clean project for a cleaning company</h2>

<h4>This project was made for a school assignment, and makes up the backend for a website for an imaginary cleaning company: StädaFint.</h3>
<p><i>Developers were Nati Schmidt, Anton Ståhlgren, Adrian Almer, Ola Magnell and Jimmy Larsson</i></p>
<br>
<p>This backend service provides functionality for creating, editing and removing users, as well as employees at the company. Employees can have one of two roles - EMPLOYEE or ADMIN. Endpoint access is granted or denied based on these roles, using Keycloak.</p>
<p>Additionaly, this backend service enables creating, updating and deleting cleaning jobs, bookable either by the customers or the employees.</p>
<p>Via JavaMailSender a gmail account is used to send confirmation emails to the user.</p>
<p>A payment service is implemented, in the form of Klarna (playground) API. This API is called using RestTemplate.</p>
<p>RestTemplate is also used to send HTTP requests to Keycloak.</p>


<h3>Endpoints in the project</h3>
<h4>Klarna</h4>
<table border="1">
  <tr>
    <th>Endpoint</th>
    <th>Argument</th>
  </tr>
  <tr>
    <td>@GetMapping("/getOrder/{id}")</td>
    <td>@PathVariable String id</td>
  </tr>
  <tr>
    <td>@PostMapping("/createOrder")</td>
    <td>KlarnaOrderDTO</td>
  </tr>
</table>
<br>
<h4>Job</h4>
<table border="1">
  <tr>
    <th>Endpoint</th>
    <th>Argument</th>
  </tr>
  <tr>
    <td>@PostMapping("/createJob")</td>
    <td>CreateJobDTO</td>
  </tr>
  <tr>
    <td>@GetMapping("/getJob")</td>
    <td>@RequestHeader Long jobId</td>
  </tr>
  <tr>
    <td>@GetMapping("/getAllJobs")</td>
    <td>n/a</td>
  </tr>
  <tr>
    <td>@GetMapping("/getAllJobsForEmployee/{empId}")</td>
    <td>@PathVariable Long empId</td>
  </tr>
  <tr>
    <td>@GetMapping("/getAllJobsForEmployeeWithStatus/{empId}")</td>
    <td>@PathVariable Long empId,RequestParam(name = "status", required = false</td>
  </tr>
  <tr>
    <td>@GetMapping("/getAllJobsForCustomer/{cusId}")</td>
    <td>@PathVariable String cusId</td>
  </tr>
  <tr>
    <td>@GetMapping("/getAllJobsForCustomerWithStatus/{cusId}")</td>
    <td>@PathVariable UUID cusId,@RequestParam(name = "statuses", required = false) List<JobStatus> status</td>
  </tr>
  <tr>
    <td>@PutMapping("/updateJob")</td>
    <td>@RequestBody UpdateJobDTO jobDTO, @RequestParam(name = "message", required = false) String message</td>
  </tr>
  <tr>
    <td>@GetMapping("/getByStatus")</td>
    <td>@RequestParam List<String> statuses</td>
  </tr>
  <tr>
    <td>@PostMapping("/getAvailableEmployees")</td>
    <td>@RequestBody GetAvailableEmployeeDTO getAvailableEmployeeDTO</td>
  </tr>
</table>



