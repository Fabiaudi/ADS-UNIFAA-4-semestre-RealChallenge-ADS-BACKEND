package com.unifaa.bookexam.model.dto;

import java.time.LocalTime;
import java.util.UUID;

/**
 * Resposta simplificada de um slot gerado para um Schedule em um dia específico.
 * ⚠️ Sem informação de capacidade/remaining (isso é responsabilidade do módulo de Availability/Bookings).
 */
public record TimeSlotResponseDTO(
        UUID scheduleId,
        String day,            // 'mon' | 'tue' | 'wed' | 'thu' | 'fri' | 'sat'
        UUID timeIntervalId,   // id do time_intervals reutilizado
        LocalTime startTime,   // ex.: 09:00
        LocalTime endTime      // ex.: 10:00
) {}
