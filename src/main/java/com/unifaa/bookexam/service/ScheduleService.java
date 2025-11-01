package com.unifaa.bookexam.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unifaa.bookexam.exception.error.BadRequestException;
import com.unifaa.bookexam.exception.error.NotFoundException;
import com.unifaa.bookexam.model.dto.ScheduleCreateDTO;
import com.unifaa.bookexam.model.dto.ScheduleResponseDTO;
import com.unifaa.bookexam.model.dto.TimeSlotResponseDTO;
import com.unifaa.bookexam.model.entity.Schedule;
import com.unifaa.bookexam.model.entity.TimeInterval;
import com.unifaa.bookexam.model.entity.TimeSlot;
import com.unifaa.bookexam.model.enums.DayOfWeek;
import com.unifaa.bookexam.repository.ScheduleRepository;
import com.unifaa.bookexam.repository.SubjectQueryRepository;
import com.unifaa.bookexam.repository.TimeIntervalRepository;
import com.unifaa.bookexam.repository.TimeSlotRepository;
import com.unifaa.bookexam.repository.UserQueryRepository;

import lombok.RequiredArgsConstructor;

/**
 * Regras de negócio de Schedule e geração de TimeSlots (bulk).
 *
 * Decisões (alinhadas):
 * - Criação de Schedule bloqueia sobreposição por {poloId, subjectId}.
 * - Geração bulk padrão (sem body):
 * seg–sex: 09:00 -> 20:00 (último bloco 19–20)
 * sábado : 09:00 -> 13:00 (último bloco 12–13)
 * passo : 60 minutos
 * - Idempotência: não duplica TimeSlot já existente
 * (checado via repository e também protegido por UNIQUE no V3).
 *
 * Integrações futuras (de outras frentes) — deixado aqui como referência:
 * - [Laís] Segurança/roles: @PreAuthorize("hasAnyRole('ADMIN','POLO')") nos
 * métodos mutadores.
 * - [Nathy] Availability/Bookings: este serviço expõe a grade (TimeSlots)
 * que será lida para compor disponibilidade por data (sem contar capacidade
 * aqui).
 */
@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final TimeIntervalRepository timeIntervalRepository;
    private final TimeSlotRepository timeSlotRepository;

    // --- Novas dependências para validações de entrada no CREATE ---
    private final SubjectQueryRepository subjectRepository; // valida se subject existe (404)
    private final UserQueryRepository userQueryRepository; // valida se polo existe e é do tipo POLO (400/404)

    // =========================
    // CRUD - CREATE Schedule
    // =========================
    @Transactional
    public ScheduleResponseDTO create(ScheduleCreateDTO dto) {
        // 1) valida datas
        validateDates(dto.startDate(), dto.endDate());

        // 2) valida subject existe → 404 (em vez de estourar FK e virar 409)
        if (dto.subjectId() == null || dto.poloId() == null) {
            throw new BadRequestException("Campos obrigatórios: poloId e subjectId.");
        }
        if (!subjectRepository.existsById(dto.subjectId())) {
            throw new NotFoundException("Disciplina não encontrada: " + dto.subjectId());
        }

        // 3) valida polo existe e é POLO
        var poloTypeOpt = userQueryRepository.findTypeById(dto.poloId());
        if (poloTypeOpt.isEmpty()) {
            throw new NotFoundException("Polo não encontrado: " + dto.poloId());
        }
        if (!"POLO".equalsIgnoreCase(poloTypeOpt.get())) {
            throw new BadRequestException("poloId precisa apontar para um usuário do tipo POLO.");
        }

        // 4) checa sobreposição p/ mesmo {poloId, subjectId}
        boolean overlaps = scheduleRepository.existsOverlapping(
                dto.poloId(), dto.subjectId(), dto.startDate(), dto.endDate());
        if (overlaps) {
            throw new BadRequestException(
                    "Já existe um período configurado para este polo e disciplina que intersecta o intervalo informado.");
        }

        // 5) persiste
        Schedule s = new Schedule();
        s.setPoloId(dto.poloId());
        s.setSubjectId(dto.subjectId());
        s.setStartDate(dto.startDate());
        s.setEndDate(dto.endDate());

        s = scheduleRepository.save(s);
        return toResponse(s);
    }

    // =========================
    // BULK - Geração padrão (60min)
    // =========================
    @Transactional
    public BulkResult generateDefaultTimeSlots(UUID scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new NotFoundException("Schedule não encontrado: " + scheduleId));

        // Apenas uma checagem defensiva de período (útil se alguém editou direto no DB)
        validateDates(schedule.getStartDate(), schedule.getEndDate());

        int created = 0;
        int skipped = 0;

        // seg–sex 09:00 -> 20:00 (19–20 último)
        List<TimeInterval> weekdayIntervals = buildIntervals(LocalTime.of(9, 0), LocalTime.of(20, 0), 60);
        // sábado 09:00 -> 13:00 (12–13 último)
        List<TimeInterval> saturdayIntervals = buildIntervals(LocalTime.of(9, 0), LocalTime.of(13, 0), 60);

        // MON..FRI
        for (DayOfWeek day : EnumSet.of(DayOfWeek.MON, DayOfWeek.TUE, DayOfWeek.WED, DayOfWeek.THU, DayOfWeek.FRI)) {
            for (TimeInterval ti : weekdayIntervals) {
                TimeInterval interval = upsertInterval(ti.getStartTime(), ti.getEndTime());
                boolean exists = timeSlotRepository.existsBySchedule_IdAndDayAndTimeInterval_Id(
                        schedule.getId(), day, interval.getId());
                if (exists) {
                    skipped++;
                    continue;
                }
                TimeSlot slot = new TimeSlot();
                slot.setSchedule(schedule);
                slot.setDay(day);
                slot.setTimeInterval(interval);
                timeSlotRepository.save(slot);
                created++;
            }
        }

        // SAT
        for (TimeInterval ti : saturdayIntervals) {
            TimeInterval interval = upsertInterval(ti.getStartTime(), ti.getEndTime());
            boolean exists = timeSlotRepository.existsBySchedule_IdAndDayAndTimeInterval_Id(
                    schedule.getId(), DayOfWeek.SAT, interval.getId());
            if (exists) {
                skipped++;
                continue;
            }
            TimeSlot slot = new TimeSlot();
            slot.setSchedule(schedule);
            slot.setDay(DayOfWeek.SAT);
            slot.setTimeInterval(interval);
            timeSlotRepository.save(slot);
            created++;
        }

        int totalNow = timeSlotRepository.countBySchedule_Id(schedule.getId());
        return new BulkResult(schedule.getId(), created, skipped, totalNow);
    }

    // =========================
    // LIST - Buscar schedules com filtros opcionais
    // =========================
    @Transactional(readOnly = true)
    public List<ScheduleResponseDTO> list(String poloId, UUID subjectId, LocalDate from, LocalDate to) {
        var items = scheduleRepository.findByFilters(poloId, subjectId, from, to);
        return items.stream()
                .map(s -> new ScheduleResponseDTO(
                        s.getId(), s.getPoloId(), s.getSubjectId(), s.getStartDate(), s.getEndDate()))
                .toList();
    }

    /**
     * Lista os TimeSlots de um Schedule para um dia específico (mon..sat).
     * - Valida existência do Schedule.
     * - Valida o dia (apenas mon..sat).
     * - Ordena por startTime (via repository).
     * - NÃO inclui capacidade/remaining (responsabilidade da Nathy).
     */
    @Transactional(readOnly = true)
    public List<TimeSlotResponseDTO> listTimeSlotsByDay(UUID scheduleId, String dayStr) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new NotFoundException("Schedule não encontrado: " + scheduleId));

        if (dayStr == null || dayStr.isBlank()) {
            throw new BadRequestException("Parâmetro 'day' é obrigatório (mon|tue|wed|thu|fri|sat).");
        }

        DayOfWeek day;
        try {
            day = DayOfWeek.from(dayStr); // aceita mon|tue|wed|thu|fri|sat (case-insensitive)
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Valor inválido para 'day'. Use: mon|tue|wed|thu|fri|sat.");
        }

        List<TimeSlot> slots = timeSlotRepository
                .findBySchedule_IdAndDayOrderByTimeInterval_StartTime(schedule.getId(), day);

        return slots.stream()
                .map(ts -> new TimeSlotResponseDTO(
                        schedule.getId(),
                        ts.getDay().name(), // 'mon'..'sat'
                        ts.getTimeInterval().getId(),
                        ts.getTimeInterval().getStartTime(),
                        ts.getTimeInterval().getEndTime()))
                .collect(Collectors.toList());
    }

    // =========================
    // DELETE Schedule
    // =========================
    @Transactional
    public void delete(UUID scheduleId) {
        if (!scheduleRepository.existsById(scheduleId)) {
            throw new NotFoundException("Schedule não encontrado: " + scheduleId);
        }
        // Observação:
        // - V1 define ON DELETE CASCADE em time_slots(schedule_id), então slots caem
        // juntos.
        scheduleRepository.deleteById(scheduleId);
    }

    // =========================
    // Helpers
    // =========================
    private void validateDates(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            throw new BadRequestException("Datas obrigatórias (startDate/endDate).");
        }
        if (!end.isAfter(start)) {
            throw new BadRequestException("endDate deve ser estritamente maior que startDate.");
        }
    }

    /**
     * Monta intervalos meio-abertos [start, end] em passos de stepMin.
     * End é o limite superior.
     */
    private List<TimeInterval> buildIntervals(LocalTime start, LocalTime endExclusive, int stepMinutes) {
        List<TimeInterval> list = new ArrayList<>();
        LocalTime cur = start;
        while (cur.isBefore(endExclusive)) {
            LocalTime next = cur.plusMinutes(stepMinutes);
            if (next.isAfter(endExclusive))
                break;
            TimeInterval ti = new TimeInterval();
            ti.setStartTime(cur);
            ti.setEndTime(next);
            list.add(ti);
            cur = next;
        }
        return list;
    }

    /** Reaproveita intervalo existente por {start,end}; se não existir, cria. */
    private TimeInterval upsertInterval(LocalTime start, LocalTime end) {
        return timeIntervalRepository.findByStartTimeAndEndTime(start, end)
                .orElseGet(() -> {
                    TimeInterval ti = new TimeInterval();
                    ti.setStartTime(start);
                    ti.setEndTime(end);
                    return timeIntervalRepository.save(ti);
                });
    }

    private ScheduleResponseDTO toResponse(Schedule s) {
        return new ScheduleResponseDTO(
                s.getId(), s.getPoloId(), s.getSubjectId(), s.getStartDate(), s.getEndDate());
    }

    // DTO de retorno do BULK (pode ser usado pelo Controller)
    public record BulkResult(UUID scheduleId, int createdCount, int skippedExistingCount, int totalSlotsForSchedule) {
    }
}
