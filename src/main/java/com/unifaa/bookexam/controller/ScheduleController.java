package com.unifaa.bookexam.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.unifaa.bookexam.model.dto.CustomDaySlotRequest;

// import org.springframework.security.access.prepost.PreAuthorize; // [Laís] Descomentar quando ligar roles

import com.unifaa.bookexam.model.dto.ScheduleCreateDTO;
import com.unifaa.bookexam.model.dto.ScheduleResponseDTO;
import com.unifaa.bookexam.model.dto.TimeSlotResponseDTO;
import com.unifaa.bookexam.service.ScheduleService;
import com.unifaa.bookexam.service.ScheduleService.BulkResult;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
@Validated
public class ScheduleController {

    private final ScheduleService scheduleService;

    /**
     * Cria um novo Schedule (janela de oferta).
     * Regras:
     * - endDate > startDate
     * - sem sobreposição para {poloId, subjectId}
     * - validações de existência/roles serão plugadas pela Laís
     */
    // @PreAuthorize("hasAnyRole('ADMIN','POLO')") // [Laís] habilitar quando
    // segurança estiver ativa
    @PostMapping
    public ResponseEntity<ScheduleResponseDTO> create(@Valid @RequestBody ScheduleCreateDTO body) {
        ScheduleResponseDTO created = scheduleService.create(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Gera a grade padrão de TimeSlots (bulk sem body) para o Schedule.
     * Padrão:
     * - seg–sex 09:00 -> 20:00 (19–20 último)
     * - sábado 09:00 -> 13:00 (12–13 último)
     * - passo 60 minutos
     * Idempotente: não duplica slots existentes.
     */
    // @PreAuthorize("hasAnyRole('ADMIN','POLO')") // [Laís]
    @PostMapping("/{id}/timeslots/bulk")
    public ResponseEntity<BulkResult> generateBulk(@PathVariable("id") UUID scheduleId) {
        BulkResult result = scheduleService.generateDefaultTimeSlots(scheduleId);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PostMapping("/{id}/timeslots/custom")
    public ResponseEntity<BulkResult> generateCustom(
            @PathVariable("id") UUID scheduleId,
            @RequestBody List<CustomDaySlotRequest> config) {

        BulkResult result = scheduleService.generateCustomTimeSlots(scheduleId, config);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping
    public ResponseEntity<List<ScheduleResponseDTO>> list(
            @RequestParam(required = false) String poloId,
            @RequestParam(required = false) UUID subjectId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        var result = scheduleService.list(poloId, subjectId, from, to);
        return ResponseEntity.ok(result);
    }

    // ===========================================================
    // GET - Lista TimeSlots por dia (apoio a Availability/Bookings)
    // ===========================================================
    @GetMapping("/{id}/timeslots")
    public ResponseEntity<List<TimeSlotResponseDTO>> listTimeSlotsByDay(
            @PathVariable("id") UUID scheduleId,
            @RequestParam("day") String day // mon|tue|wed|thu|fri|sat
    ) {
        // // Segurança (Laís) - descomentar quando roles estiverem ativas:
        // @PreAuthorize("hasAnyRole('ADMIN','POLO','STUDENT')")
        var items = scheduleService.listTimeSlotsByDay(scheduleId, day);
        return ResponseEntity.ok(items);
    }

    /**
     * Exclui um Schedule por ID.
     * Observação:
     * - time_slots caem por cascade (V1)
     * - bookings não estão ligados a schedule (MVP), então não sofrem alteração
     */
    // @PreAuthorize("hasAnyRole('ADMIN','POLO')") // [Laís]
    @DeleteMapping("/{id}")

    public ResponseEntity<Void> delete(@PathVariable("id") UUID scheduleId) {
        scheduleService.delete(scheduleId);
        return ResponseEntity.noContent().build();
    }
}
