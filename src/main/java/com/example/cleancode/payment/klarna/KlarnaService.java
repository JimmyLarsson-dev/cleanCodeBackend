package com.example.cleancode.payment.klarna;

import com.example.cleancode.job.Job;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KlarnaService {
    @Autowired
    private EntityManager entityManager;


}
