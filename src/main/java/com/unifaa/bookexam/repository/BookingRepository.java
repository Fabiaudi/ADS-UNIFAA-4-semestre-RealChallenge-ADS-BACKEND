package com.unifaa.bookexam.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unifaa.bookexam.model.entity.Booking;
import com.unifaa.bookexam.model.entity.Polo;
import com.unifaa.bookexam.model.entity.Subject;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID>{

    List<Booking> findByStudentId(String studentId);
         
    long countByPoloAndSubjectAndDateAndTime(
        Polo polo,
        Subject subject,
        LocalDate date,
        LocalTime time
    );

    Optional<Booking> findByStudentIdAndSubjectAndPoloAndDateAndTime(
        String studentId,
        Subject subject,
        Polo polo,
        LocalDate date,
        LocalTime time
    );
}
