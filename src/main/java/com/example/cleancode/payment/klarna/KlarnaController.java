package com.example.cleancode.payment.klarna;

import com.example.cleancode.job.Job;
import com.example.cleancode.job.JobRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

@RestController
@RequestMapping("/api/klarna")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class KlarnaController {

    @Value("${klarna.username}")
    private String username;

    @Value("${klarna.password}")
    private String password;


    private final KlarnaService klarnaService;
    private final JobRepository jobRepository;

    public KlarnaController(KlarnaService klarnaService, JobRepository jobRepository) {
        this.klarnaService = klarnaService;
        this.jobRepository = jobRepository;
    }


    @GetMapping("/getOrder/{id}")
    public ResponseEntity<Map<String, Object>> getOrder (@PathVariable String id){
        // Create the headers
        HttpHeaders headers = new HttpHeaders();
        String credentials = username + ":" + password;
        String encodedCredentials = new String(Base64.getEncoder().encode(credentials.getBytes()));
        headers.set("Authorization", "Basic " + encodedCredentials);
        headers.set("Content-Type", "application/json");

        // Create the HttpEntity object
        HttpEntity<KlarnaOrderDTO> entity = new HttpEntity<>(headers);
        // Create RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();

        // Send POST request
        ResponseEntity<Map> response = restTemplate.exchange(
                "https://api.playground.klarna.com/checkout/v3/orders/" + id,
                HttpMethod.GET,
                entity,
                Map.class
        );
        // Check for successful response
        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok(response.getBody());
        } else {
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        }
    }
    @PostMapping("/createOrder")
    public ResponseEntity<Map<String, Object>> createOrder(@RequestBody KlarnaOrderDTO klarnaOrder) {
        // Create the headers
        HttpHeaders headers = new HttpHeaders();
        String credentials = username + ":" + password;
        String encodedCredentials = new String(Base64.getEncoder().encode(credentials.getBytes()));
        headers.set("Authorization", "Basic " + encodedCredentials);
        headers.set("Content-Type", "application/json");

        // Create the HttpEntity object
        HttpEntity<KlarnaOrderDTO> entity = new HttpEntity<>(klarnaOrder, headers);

        // Create RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();

        // Send POST request
        ResponseEntity<Map> response = restTemplate.exchange(
                "https://api.playground.klarna.com/checkout/v3/orders",
                HttpMethod.POST,
                entity,
                Map.class
        );

        // Check for successful response
        if (response.getStatusCode() == HttpStatus.CREATED) {

            JSONObject jsonResponse = new JSONObject(response.getBody());
            String orderId = jsonResponse.getString("order_id");
            String htmlSnippet = jsonResponse.getString("html_snippet");
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Order created successfully");
            responseBody.put("orderID", orderId);
            responseBody.put("htmlSnippet", htmlSnippet);


            return ResponseEntity.ok(responseBody);
        } else {
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        }
    }
}


