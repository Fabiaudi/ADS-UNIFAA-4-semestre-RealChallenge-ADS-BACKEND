package com.unifaa.bookexam.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unifaa.bookexam.model.entity.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID>{

    List<Booking> findByStudentId(String studentId);

}
