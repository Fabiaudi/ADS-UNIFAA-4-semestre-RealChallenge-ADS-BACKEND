-- ============================
-- V1__init.sql  (UNIFAA - BookExam)
-- ============================

-- USERS (Herança: User / Student / Admin / Polo)
-- id = TEXT (matrícula/identificador)
CREATE TABLE IF NOT EXISTS users (
  id                TEXT PRIMARY KEY,
  name              VARCHAR(120) NOT NULL,
  email             VARCHAR(160) UNIQUE NOT NULL,
  password_hash     VARCHAR(200) NOT NULL,
  type              VARCHAR(10)  NOT NULL CHECK (type IN ('STUDENT','ADMIN','POLO')),

  -- Student
  course            VARCHAR(120),
  student_polo_id   TEXT,            -- FK para um user do tipo POLO

  -- Polo
  location          VARCHAR(200),
  availability      INT,

  created_at        TIMESTAMP NOT NULL DEFAULT NOW()
);

ALTER TABLE users
  ADD CONSTRAINT fk_student_polo
  FOREIGN KEY (student_polo_id) REFERENCES users(id)
  ON DELETE SET NULL;

CREATE INDEX IF NOT EXISTS idx_users_type ON users(type);
CREATE INDEX IF NOT EXISTS idx_users_student_polo ON users(student_polo_id);

-- SUBJECTS
CREATE TABLE IF NOT EXISTS subjects (
  id    UUID PRIMARY KEY,
  name  VARCHAR(140) NOT NULL
);

-- SCHEDULES (período por Polo + Subject)
CREATE TABLE IF NOT EXISTS schedules (
  id          UUID PRIMARY KEY,
  polo_id     TEXT NOT NULL REFERENCES users(id) ON DELETE CASCADE,  -- deve ser um user POLO
  subject_id  UUID NOT NULL REFERENCES subjects(id) ON DELETE CASCADE,
  start_date  DATE NOT NULL,
  end_date    DATE NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_schedules_polo    ON schedules(polo_id);
CREATE INDEX IF NOT EXISTS idx_schedules_subject ON schedules(subject_id);

-- TIME INTERVALS (09:00–10:00, etc.)
CREATE TABLE IF NOT EXISTS time_intervals (
  id          UUID PRIMARY KEY,
  start_time  TIME NOT NULL,
  end_time    TIME NOT NULL,
  CONSTRAINT chk_interval_gt CHECK (end_time > start_time)
);

-- TIME SLOTS (dia da semana + intervalo) pertencem a um Schedule
-- Enum de dia representado por VARCHAR(3) + CHECK (mon..sat)
CREATE TABLE IF NOT EXISTS time_slots (
  id               UUID PRIMARY KEY,
  schedule_id      UUID NOT NULL REFERENCES schedules(id) ON DELETE CASCADE,
  day              VARCHAR(3) NOT NULL CHECK (day IN ('mon','tue','wed','thu','fri','sat')),
  time_interval_id UUID NOT NULL REFERENCES time_intervals(id) ON DELETE RESTRICT
);

CREATE INDEX IF NOT EXISTS idx_time_slots_schedule ON time_slots(schedule_id);
CREATE INDEX IF NOT EXISTS idx_time_slots_day      ON time_slots(day);

-- BOOKINGS (agendamento concreto)
CREATE TABLE IF NOT EXISTS bookings (
  id          UUID PRIMARY KEY,
  polo_id     TEXT NOT NULL REFERENCES users(id) ON DELETE RESTRICT,    -- Polo onde ocorre
  student_id  TEXT NOT NULL REFERENCES users(id) ON DELETE CASCADE,     -- Aluno
  subject_id  UUID NOT NULL REFERENCES subjects(id) ON DELETE RESTRICT, -- Disciplina

  created_at  TIMESTAMP NOT NULL DEFAULT NOW(),
  date        DATE NOT NULL,
  time        TIME NOT NULL,

  -- Evita duplicidade para o mesmo aluno na mesma matéria/horário/polo
  CONSTRAINT uq_booking_per_student UNIQUE (student_id, subject_id, polo_id, date, time)
);

CREATE INDEX IF NOT EXISTS idx_bookings_polo_date ON bookings(polo_id, date);
CREATE INDEX IF NOT EXISTS idx_bookings_student   ON bookings(student_id);
CREATE INDEX IF NOT EXISTS idx_bookings_subject   ON bookings(subject_id);