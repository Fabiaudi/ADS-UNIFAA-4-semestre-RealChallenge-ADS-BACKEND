package com.unifaa.bookexam.model.dto;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO de entrada para criar uma janela de oferta (Schedule).
 *
 * Regras de negócio (validadas no Service):
 * - endDate deve ser estritamente após startDate.
 * - bloquear sobreposição para o mesmo par {poloId, subjectId}.
 * - verificar se polo existe e é do tipo POLO.
 * - verificar se subject existe.
 *
 * Observação:
 * - Segurança/roles (ADMIN/POLO) será adicionada pela Laís; aqui apenas
 * definimos o contrato.
 */
public record ScheduleCreateDTO(
        @NotNull @Size(min = 1, max = 20) String poloId,
        @NotNull UUID subjectId,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate) {
}
