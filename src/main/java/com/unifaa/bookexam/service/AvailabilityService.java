package com.unifaa.bookexam.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.unifaa.bookexam.model.dto.AvailabilitySlotDTO;
import com.unifaa.bookexam.model.entity.Polo;
import com.unifaa.bookexam.model.entity.Schedule;
import com.unifaa.bookexam.model.entity.Subject;
import com.unifaa.bookexam.model.entity.TimeSlot;
import com.unifaa.bookexam.repository.BookingRepository;
import com.unifaa.bookexam.repository.PoloRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AvailabilityService {

    private final ScheduleService scheduleService;
    private final TimeSlotService timeSlotService;
    private final BookingRepository bookingRepository;
    private final PoloService poloService;
    private final PoloRepository poloRepository;

 /**
     * Monta lista de AvailabilitySlotDTO para um subject+polo+date.
     */
    public List<AvailabilitySlotDTO> getAvailability(Subject subject, String poloId, LocalDate date) {

            System.out.println("=== DEBUG AVAILABILITY ===");
            System.out.println("Subject: " + subject.getId() + " - " + subject.getName());
            System.out.println("Polo ID: " + poloId);
            System.out.println("Date: " + date);

          // 1 Busca o schedule ativo na data
        Optional<Schedule> scheduleOpt = scheduleService.findScheduleForSubjectAndPoloOnDate(poloId, subject.getId(), date);
        if (scheduleOpt.isEmpty()) return List.of(); // sem schedule -> sem disponibilidade

        Schedule schedule = scheduleOpt.get(); // pega o Schedule do Optional
            System.out.println("Schedule ID: " + schedule.getId());

        // 2 Determina o dia da semana compatível com seu enum customizado
       com.unifaa.bookexam.model.enums.DayOfWeek dow =
            com.unifaa.bookexam.model.enums.DayOfWeek.fromJava(date.getDayOfWeek());
            System.out.println("Day of Week: " + dow);
        
        // 3 Busca os time slots do schedule para aquele dia
        UUID scheduleId = schedule.getId();
        List<TimeSlot> timeSlots = timeSlotService.findByScheduleIdAndDay(scheduleId, dow);
         System.out.println("TimeSlots encontrados: " + timeSlots.size());

        // 4 Busca capacidade do polo
        //UUID poloUuid = UUID.fromString(poloId);
        int capacity = poloService.getCapacity(poloId);
         System.out.println("Capacidade do polo: " + capacity);

        Polo polo = poloRepository.findById(poloId)
        .orElseThrow(() -> new IllegalArgumentException("Polo não encontrado"));

        // 5 Monta os DTOs com disponibilidade real
        List<AvailabilitySlotDTO> result = new ArrayList<>();
        for (TimeSlot ts : timeSlots) {
            LocalTime slotStart = ts.getTimeInterval().getStartTime();
            
            long booked = bookingRepository.countByPolo_IdAndSubjectAndDateAndTime(
                polo,
                subject,
                date,
                slotStart
            );

            System.out.println("TimeSlot " + slotStart + " - Booked: " + booked + " - Available: " + (capacity - booked));

            AvailabilitySlotDTO dto = new AvailabilitySlotDTO();
            
            dto.setPoloId(poloId.toString());
            dto.setSubject(subject);
            dto.setDate(date);
            dto.setTime(slotStart);
            dto.setCapacity(capacity);
            dto.setBooked((int) booked);
            dto.setAvailable(Math.max(0, capacity - (int) booked));
            dto.setHasAvailability(booked < capacity);

            result.add(dto);
        }

            System.out.println("Total de DTOs retornados: " + result.size());
        return result;
    }
}

