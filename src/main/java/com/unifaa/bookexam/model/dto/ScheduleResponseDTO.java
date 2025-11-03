package com.unifaa.bookexam.model.dto;

import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO de saída enxuto para retornar um Schedule criado/consultado.
 * Mantemos somente os campos necessários ao front.
 */
public record ScheduleResponseDTO(
        UUID id,
        String poloId,
        UUID subjectId,
        LocalDate startDate,
        LocalDate endDate
) {}
