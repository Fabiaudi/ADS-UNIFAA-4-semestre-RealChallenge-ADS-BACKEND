package com.unifaa.bookexam.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unifaa.bookexam.model.entity.Booking;
import com.unifaa.bookexam.model.enums.BookingStatus;
import com.unifaa.bookexam.repository.BookingRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository repository;
    
    public Booking reservar(Booking booking) {
        return repository.save(booking);
    }

    @Transactional
    public List<Booking> findMine(String studentId) {
    return repository.findByStudentId(studentId);
    }

    @Transactional
    public void cancelBooking(String requesterId, boolean requesterIsAdminOrPolo, String bookingId) {
        UUID bookingUuid = UUID.fromString(bookingId);

        Booking booking = repository.findById(bookingUuid)
            .orElseThrow(() -> new IllegalArgumentException("Booking não encontrado."));

        // apenas o admin/polo pode cancelar
        if (!requesterIsAdminOrPolo && !booking.getStudentId().equals(requesterId)) {
            throw new SecurityException("Usuário não autorizado a cancelar esse booking.");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        repository.save(booking);
    }

    // scheduleService
    // preciso de outras classes para implementar esse

}