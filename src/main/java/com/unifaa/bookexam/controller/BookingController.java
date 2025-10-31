package com.unifaa.bookexam.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.unifaa.bookexam.model.dto.BookingRequestDTO;
import com.unifaa.bookexam.model.dto.BookingResponseDTO;
import com.unifaa.bookexam.model.entity.Booking;
import com.unifaa.bookexam.model.mappers.BookingsMappers;
import com.unifaa.bookexam.security.CustomUserPrincipal;
import com.unifaa.bookexam.service.BookingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingsMappers mapper;
    private final BookingService service;
    
    @PostMapping
    // @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<BookingRequestDTO> createBooking(@RequestBody BookingRequestDTO bookingRequestDTO) {

        Booking booking = mapper.toEntity(bookingRequestDTO);
        Booking savedBooking = service.reservar(booking);

        BookingRequestDTO requestDTO = mapper.requestToDTO(savedBooking);
    
        return ResponseEntity.ok(requestDTO);
    }

    // @DeleteMapping("/{id}")
    // public ResponseEntity<Void> cancelBooking(@PathVariable String id, @AuthenticationPrincipal CustomUserPrincipal principal) {
    //     boolean isAdminOrPolo = principal.hasRole("ADMIN") || principal.hasRole("POLO");
    //     service.cancelBooking(principal.getId(), isAdminOrPolo, id);
    //     return ResponseEntity.noContent().build();
    // } preciso da parte de Autenticação para implementar esse método

    @GetMapping("/mine")
    public ResponseEntity<List<BookingResponseDTO>> getMineBooking(@RequestParam String studentId) {
        var bookings = service.findMine(studentId);
        var dtos = bookings.stream()
                           .map(mapper::toDTO)
                           .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}