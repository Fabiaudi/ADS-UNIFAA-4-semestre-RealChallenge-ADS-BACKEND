package com.unifaa.bookexam.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.unifaa.bookexam.model.entity.Schedule;

/**
 * Repositório de Schedules (mapeado por IDs simples).
 *
 * - existsOverlapping(...) bloqueia criação com interseção de período
 * para o mesmo par {poloId, subjectId}.
 * - findByFilters(...) lista com filtros opcionais para o endpoint GET.
 */
public interface ScheduleRepository extends JpaRepository<Schedule, UUID> {

  /**
   * Interseção verdadeira quando:
   * novo.start <= existente.end AND novo.end >= existente.start
   */
  @Query("""
          SELECT CASE WHEN COUNT(s) > 0 THEN TRUE ELSE FALSE END
          FROM Schedule s
          WHERE s.poloId = :poloId
            AND s.subjectId = :subjectId
            AND (:startDate <= s.endDate AND :endDate >= s.startDate)
      """)
  boolean existsOverlapping(String poloId, UUID subjectId, LocalDate startDate, LocalDate endDate);

  /**
   * Filtros opcionais:
   * - poloId / subjectId (igualdade quando informados)
   * - from: inclui agendas cujo endDate >= from
   * - to : inclui agendas cujo startDate <= to
   *
   * Isso retorna qualquer schedule que tenha ALGUMA interseção com o intervalo
   * [from, to],
   * respeitando os filtros por polo/subject quando presentes.
   */
  @Query("""
          SELECT s
          FROM Schedule s
          WHERE (:poloId IS NULL OR s.poloId = :poloId)
            AND (:subjectId IS NULL OR s.subjectId = :subjectId)
            AND (:from IS NULL OR s.endDate >= :from)
            AND (:to   IS NULL OR s.startDate <= :to)
          ORDER BY s.startDate ASC, s.endDate ASC
      """)
  List<Schedule> findByFilters(String poloId, UUID subjectId, LocalDate from, LocalDate to);

  Optional<Schedule> findByPoloIdAndSubjectIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
    String poloId, UUID subjectId, LocalDate startDate, LocalDate endDate);
}
