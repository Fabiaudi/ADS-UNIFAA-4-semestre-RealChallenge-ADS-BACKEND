-- ============================================
-- V4__unique_time_intervals.sql
-- UNIFAA - BookExam
-- Objetivo:
-- 1) Remover registros duplicados em public.time_intervals com mesmo (start_time, end_time)
--    mantendo uma única linha por par.
-- 2) Adicionar constraint UNIQUE (start_time, end_time) de forma idempotente.
-- ============================================

-- 1) Deduplicação de time_intervals por (start_time, end_time)
WITH duplicados AS (
  SELECT
    id,
    start_time,
    end_time,
    ROW_NUMBER() OVER (PARTITION BY start_time, end_time ORDER BY id) AS rn
  FROM public.time_intervals
)
DELETE FROM public.time_intervals ti
USING duplicados d
WHERE ti.id = d.id
  AND d.rn > 1;

-- 2) Adicionar constraint UNIQUE (idempotente) para evitar novas duplicatas
DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1
    FROM pg_constraint
    WHERE conname = 'uq_time_intervals_start_end'
      AND conrelid = 'public.time_intervals'::regclass
  ) THEN
    ALTER TABLE public.time_intervals
      ADD CONSTRAINT uq_time_intervals_start_end
      UNIQUE (start_time, end_time);
  END IF;
END $$;
