package com.unifaa.bookexam.repository;

import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unifaa.bookexam.model.entity.TimeInterval;

/**
 * Repositório de TimeInterval.
 * - Reaproveitamos intervalos existentes pelo par {startTime, endTime}
 * para manter o banco limpo.
 */
public interface TimeIntervalRepository extends JpaRepository<TimeInterval, UUID> {

    Optional<TimeInterval> findByStartTimeAndEndTime(LocalTime startTime, LocalTime endTime);
}
