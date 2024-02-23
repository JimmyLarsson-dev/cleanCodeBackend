package com.example.cleancode.job;

import com.example.cleancode.enums.JobStatus;
import com.example.cleancode.enums.TimeSlots;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {

    @Query("SELECT j FROM Job j JOIN j.booked a WHERE a.date = :date AND a.timeSlots = :timeSlots")
    List<Job> findJobsInTimeRange(@Param("date") LocalDateTime date, @Param("timeSlots") TimeSlots timeSlots);

    List<Job> findByJobStatusIn(List<JobStatus> statuses);

    Job findByJobId(Long jobId);

}
