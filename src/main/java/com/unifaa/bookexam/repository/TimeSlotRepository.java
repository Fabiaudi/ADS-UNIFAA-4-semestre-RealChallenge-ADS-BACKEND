package com.unifaa.bookexam.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unifaa.bookexam.model.entity.TimeSlot;
import com.unifaa.bookexam.model.enums.DayOfWeek;

/**
 * Repositório de TimeSlot.
 *
 * Observações:
 * - Usa navegação por IDs dos relacionamentos (@ManyToOne):
 * - schedule.id -> Schedule_Id
 * - timeInterval.id -> TimeInterval_Id
 * - Esses métodos ajudam na idempotência do bulk e em métricas rápidas.
 */
public interface TimeSlotRepository extends JpaRepository<TimeSlot, UUID> {

    boolean existsBySchedule_IdAndDayAndTimeInterval_Id(UUID scheduleId, DayOfWeek day, UUID timeIntervalId);

    int countBySchedule_Id(UUID scheduleId);

    List<TimeSlot> findBySchedule_IdAndDayOrderByTimeInterval_StartTime(UUID scheduleId, DayOfWeek day);

}
