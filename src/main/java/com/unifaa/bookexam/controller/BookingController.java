package com.unifaa.bookexam.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.unifaa.bookexam.model.dto.AvailabilitySlotDTO;
import com.unifaa.bookexam.model.dto.BookingRequestDTO;
import com.unifaa.bookexam.model.dto.BookingResponseDTO;
import com.unifaa.bookexam.model.entity.Booking;
import com.unifaa.bookexam.model.entity.Subject;
import com.unifaa.bookexam.model.mappers.BookingsMappers;
import com.unifaa.bookexam.service.AvailabilityService;
import com.unifaa.bookexam.service.BookingService;
import com.unifaa.bookexam.service.SubjectService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingsMappers mapper;
    private final BookingService service;
    private final AvailabilityService availabilityService;
    private final SubjectService subjectService;
    
    @PostMapping
    //@PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<BookingResponseDTO> createBooking(@RequestBody BookingRequestDTO dto) {

        Booking booking = service.createBooking(dto.getStudentId(), dto);
        BookingResponseDTO response = mapper.toDTO(booking);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(
            @PathVariable("id") String id,
            Authentication authentication) {

        String userId = authentication.getName();
        boolean isAdminOrPolo = authentication.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN") || role.getAuthority().equals("ROLE_POLO"));

        service.deleteBooking(userId, isAdminOrPolo, id);

        return ResponseEntity.noContent().build();
    } //preciso da parte de Autenticação para implementar esse método


    @GetMapping("/mine")
    //@PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<BookingResponseDTO>> getMyBookings(@RequestParam String studentId) {
        
        //String studentId = authentication.getName();

        var bookings = service.getMyBookings(studentId);
        var dtos = bookings.stream().map(mapper::toDTO).toList();
        
        return ResponseEntity.ok(dtos);
    }

    // @GetMapping("/all-bookings-polo")
    // public ResponseEntity<List<BookingResponseDTO>> getAllBookingsPolo(@RequestParam String poloId) {
    //     UUID poloUuid;
    //     try {
    //         poloUuid = UUID.fromString(poloId); // converte para UUID
    //     } catch (IllegalArgumentException e) {
    //         return ResponseEntity.badRequest().build(); // caso o parâmetro seja inválido
    //     }

    //     List<Booking> bookings = service.getAllBookingsPolo(poloUuid);

    //         if (bookings.isEmpty()) {
    //             return ResponseEntity.noContent().build(); // retorna 204 se não tiver reservas
    //         }

    //         List<BookingResponseDTO> dtos = bookings.stream()
    //             .map(mapper::toDTO)
    //             .collect(Collectors.toList());

    //         return ResponseEntity.ok(dtos);
    //     }

        @GetMapping("/availability")
        public ResponseEntity<List<AvailabilitySlotDTO>> getAvailability(
                @RequestParam UUID subject,
                @RequestParam String poloId,
                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

            // Busca o Subject pelo ID
            Subject subjectObj = subjectService.findById(subject)
                .orElseThrow(() -> new EntityNotFoundException("Subject não encontrado"));

            List<AvailabilitySlotDTO> slots = availabilityService.getAvailability(subjectObj, poloId, date);

            if (slots.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(slots);
        }
}