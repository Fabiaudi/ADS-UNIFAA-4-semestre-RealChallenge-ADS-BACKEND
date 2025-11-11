package com.unifaa.bookexam.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.unifaa.bookexam.model.dto.AvailabilitySlotDTO;
import com.unifaa.bookexam.model.dto.BookingRequestDTO;
import com.unifaa.bookexam.model.dto.BookingResponseDTO;
import com.unifaa.bookexam.model.entity.Booking;
import com.unifaa.bookexam.model.entity.Schedule;
import com.unifaa.bookexam.model.entity.Subject;
import com.unifaa.bookexam.model.mappers.BookingsMappers;
import com.unifaa.bookexam.service.AvailabilityService;
import com.unifaa.bookexam.service.BookingService;
import com.unifaa.bookexam.service.ScheduleService;
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
    private final ScheduleService scheduleService;
    
    @PostMapping
    //@PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<BookingRequestDTO> createBooking(@RequestBody BookingRequestDTO bookingRequestDTO) {

        Booking booking = mapper.toEntity(bookingRequestDTO);
        Booking savedBooking = service.reservar(booking);

        BookingRequestDTO requestDTO = mapper.requestToDTO(savedBooking);
    
        return ResponseEntity.ok(requestDTO);
    }

    // @DeleteMapping("/{id}")
    // public ResponseEntity<Void> cancelBooking(@PathVariable("id") String id) {
    //     boolean isAdminOrPolo = principal.hasRole("ADMIN") || principal.hasRole("POLO");
    //     service.cancelBooking(principal.getId(), isAdminOrPolo, id);
    //     return ResponseEntity.noContent().build();
    // } preciso da parte de Autenticação para implementar esse método

    @GetMapping("/mine")
    public ResponseEntity<List<BookingResponseDTO>> getMineBooking(@RequestParam String studentId) {
        var bookings = service.getMyBookings(studentId);
        var dtos = bookings.stream()
                           .map(mapper::toDTO)
                           .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/all-bookings-polo")
    public ResponseEntity<List<BookingResponseDTO>> getAllBookingsPolo(@RequestParam String poloId) {
        UUID poloUuid;
        try {
            poloUuid = UUID.fromString(poloId); // converte para UUID
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build(); // caso o parâmetro seja inválido
        }

        List<Booking> bookings = service.getAllBookingsPolo(poloUuid);

            if (bookings.isEmpty()) {
                return ResponseEntity.noContent().build(); // retorna 204 se não tiver reservas
            }

            List<BookingResponseDTO> dtos = bookings.stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());

            return ResponseEntity.ok(dtos);
        }

        @GetMapping("/availability") //fica
        public ResponseEntity<List<AvailabilitySlotDTO>> getAvailability(
                @RequestParam UUID subject,
                @RequestParam UUID poloId,
                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

            // Busca o Subject pelo ID
            Subject subjectObj = subjectService.findById(subject)
                .orElseThrow(() -> new EntityNotFoundException("Subject não encontrado"));

            List<AvailabilitySlotDTO> slots = availabilityService.getAvailability(subjectObj, poloId, date);

            if (slots.isEmpty()) {
                return ResponseEntity.noContent().build(); // retorna 204 se não houver janelas
            }

            return ResponseEntity.ok(slots);
        }

        @GetMapping("/debug-schedule")
        public ResponseEntity<?> debugSchedule(
        @RequestParam UUID subjectId,
        @RequestParam UUID poloId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
    
    Subject subject = subjectService.findById(subjectId)
        .orElseThrow(() -> new EntityNotFoundException("Subject não encontrado"));
    
    Optional<Schedule> schedule = scheduleService.findScheduleForSubjectAndPoloOnDate(poloId, subjectId, date);
    
    Map<String, Object> debug = new HashMap<>();
    debug.put("subject", subject);
    debug.put("poloId", poloId);
    debug.put("date", date);
    debug.put("scheduleFound", schedule.isPresent());
    debug.put("schedule", schedule.orElse(null));
    
    return ResponseEntity.ok(debug);
}
}