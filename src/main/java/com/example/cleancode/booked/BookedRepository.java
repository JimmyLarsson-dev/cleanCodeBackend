package com.example.cleancode.booked;

import com.example.cleancode.enums.TimeSlots;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface BookedRepository extends JpaRepository<Booked, Long> {
    Optional<Booked> findByDateAndTimeSlots(Date date, TimeSlots timeSlot);

    List<Booked> findByDate(LocalDateTime localDateTime);

}
