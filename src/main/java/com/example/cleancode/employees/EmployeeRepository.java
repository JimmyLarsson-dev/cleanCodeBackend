package com.example.cleancode.employees;

import com.example.cleancode.enums.Role;
import com.example.cleancode.enums.TimeSlots;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findBySsNumber(String ssNumber);

    @Query("SELECT e FROM Employee e WHERE e.id NOT IN (SELECT ae.id FROM Booked a " +
            "JOIN a.employees ae WHERE a.date = ?1 AND a.timeSlots = ?2)")
    List<Employee> findUnbookedEmployees(Date date, TimeSlots timeSlot);

    List<Employee> findByRole(Role role);

    Optional<Employee> findByEmail(String email);

}
