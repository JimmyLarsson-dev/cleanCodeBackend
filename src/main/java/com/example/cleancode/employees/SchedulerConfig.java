package com.example.cleancode.employees;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
@Configuration
@EnableScheduling
public class SchedulerConfig {

    @Autowired
    private MonthlyResetService monthlyResetService;

    @Scheduled(cron = "0 0 0 1 * ?") // Körs vid midnatt när det gått över till en ny månad
    public void resetMonthlyValues() {
        monthlyResetService.resetMonthlyValues();
    }
}
