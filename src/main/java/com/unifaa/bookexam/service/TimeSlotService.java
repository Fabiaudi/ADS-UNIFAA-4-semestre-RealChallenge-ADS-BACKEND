package com.unifaa.bookexam.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unifaa.bookexam.model.entity.TimeSlot;
import com.unifaa.bookexam.repository.TimeSlotRepository;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;


@Service
public class TimeSlotService {

    private final TimeSlotRepository timeSlotRepository;

    public TimeSlotService(TimeSlotRepository timeSlotRepository) {
        this.timeSlotRepository = timeSlotRepository;
    }

    /**
     * Retorna todos os time slots de um schedule específico
     *
     * @param scheduleId - ID do schedule
     * @return lista de TimeSlot
     */
    @Transactional(readOnly = true)
    public List<TimeSlot> findByScheduleId(UUID scheduleId) {
        return timeSlotRepository.findByScheduleId(scheduleId);
    }

    /**
     * Retorna todos os time slots de um schedule em um dia específico da semana
     *
     * @param scheduleId - ID do schedule
     * @param dayOfWeek  - dia da semana (MONDAY, TUESDAY, etc.)
     * @return lista de TimeSlot
     */
    @Transactional(readOnly = true)
    public List<TimeSlot> findByScheduleIdAndDay(UUID scheduleId, com.unifaa.bookexam.model.enums.DayOfWeek dayOfWeek) {
        return timeSlotRepository.findBySchedule_IdAndDayOrderByTimeInterval_StartTime(scheduleId, dayOfWeek);
    }

    public boolean existsByScheduleAndDayAndStartTime(UUID scheduleId, com.unifaa.bookexam.model.enums.DayOfWeek day, LocalTime startTime) {
        return timeSlotRepository.existsBySchedule_IdAndDayAndTimeInterval_StartTime(scheduleId, day, startTime);
}

}