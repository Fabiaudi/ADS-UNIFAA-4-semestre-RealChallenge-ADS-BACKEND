-- ============================================
-- V3__unique_timeslot.sql
-- Garante integridade: não pode haver dois slots
-- iguais para o mesmo (schedule, day, time_interval)
-- ============================================

-- (Opcional, seguro): remover duplicatas existentes mantendo o menor id
WITH dups AS (
  SELECT schedule_id, day, time_interval_id, MIN(id::text)::uuid AS keep_id
  FROM time_slots
  GROUP BY schedule_id, day, time_interval_id
  HAVING COUNT(*) > 1
)
DELETE FROM time_slots t
USING dups d
WHERE t.schedule_id = d.schedule_id
  AND t.day = d.day
  AND t.time_interval_id = d.time_interval_id
  AND t.id <> d.keep_id;

-- Constraint de unicidade (evita duplicação concorrente)
ALTER TABLE time_slots
  ADD CONSTRAINT uq_time_slots_schedule_day_interval
  UNIQUE (schedule_id, day, time_interval_id);
