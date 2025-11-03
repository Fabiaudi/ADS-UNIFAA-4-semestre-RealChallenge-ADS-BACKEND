# 🧩 Módulo: Schedule & TimeSlots  
**Responsável:** Fabiana Audi  
**Contexto:** Módulo de gerenciamento de agendas de prova (Schedules) e geração automática de horários (TimeSlots) no sistema **BookExam**.  

---

## ⚙️ Estrutura Geral

### Entidades Principais
| Entidade | Tabela | Descrição |
|-----------|---------|------------|
| `Schedule` | `schedules` | Define o período de aplicação de uma disciplina em um polo. |
| `TimeSlot` | `time_slots` | Representa um horário/dia de prova associado a um `Schedule`. |
| `TimeInterval` | `time_intervals` | Tabela de referência para intervalos de tempo reutilizáveis (09:00–10:00, etc.). |

---

## 🧠 Regras de Negócio

### 🔹 Schedule
- Um **Schedule** é único por `{poloId, subjectId}` no intervalo informado (não pode haver sobreposição).
- `poloId` deve referenciar um **usuário do tipo POLO**.
- `subjectId` deve referenciar uma disciplina existente.
- Apenas **usuários com role ADMIN** podem criar e deletar schedules (validação futura via módulo de segurança – Laís).
- Exclusão de um `Schedule` deleta automaticamente todos os `TimeSlots` relacionados (via `ON DELETE CASCADE`).

### 🔹 TimeSlots (geração automática)
- Gerados via endpoint `/api/schedules/{id}/timeslots/bulk`.
- Criação padrão:
  - **Seg–Sex:** 09:00 → 20:00 (último bloco 19–20)
  - **Sáb:** 09:00 → 13:00 (último bloco 12–13)
  - Intervalo fixo de **60 minutos**.
- Idempotência: não duplica time slots já existentes.
- Reutiliza intervalos existentes em `time_intervals` (controlado por UNIQUE + V4).

---

## 🧩 Migrations Flyway
| Versão | Nome | Objetivo |
|---------|------|-----------|
| `V1__init.sql` | Estrutura inicial (schedules, time_slots, time_intervals) |
| `V2__seed_minimal.sql` | Seed de dados básicos (users, subjects) |
| `V3__unique_timeslot.sql` | Constraint única `{schedule_id, day, time_interval_id}` |
| `V4__unique_time_intervals.sql` | Impede duplicação de `{start_time, end_time}` |

✅ **Status atual:**  
```
Flyway Schema History
──────────────────────────────────────────────────────
Version 1 – Success (init)
Version 2 – Success (seed minimal)
Version 3 – Success (unique_timeslot)
Version 4 – Success (unique_time_intervals)
```

---

## 📡 Endpoints Implementados

### **1️⃣ Criar Schedule**
`POST /api/schedules`

**Body:**
```json
{
  "poloId": "P00001",
  "subjectId": "10000000-0000-0000-0000-000000000004",
  "startDate": "2025-11-01",
  "endDate": "2025-12-01"
}
```

**Respostas:**
| Status | Descrição |
|---------|------------|
| `201 Created` | Schedule criado com sucesso |
| `400 Bad Request` | Polo inválido / período sobreposto / datas incorretas |
| `404 Not Found` | Disciplina inexistente |

---

### **2️⃣ Gerar TimeSlots padrão**
`POST /api/schedules/{id}/timeslots/bulk`

**Resposta:**
```json
{
  "scheduleId": "de0961fe-6246-4b64-97fd-8cb79d9cb612",
  "createdCount": 66,
  "skippedExistingCount": 0,
  "totalSlotsForSchedule": 66
}
```

---

### **3️⃣ Listar Schedules**
`GET /api/schedules?poloId=&subjectId=&from=&to=`

Retorna lista de `ScheduleResponseDTO` com filtros opcionais.

---

### **4️⃣ Listar TimeSlots de um dia**
`GET /api/schedules/{id}/timeslots?day=mon`

**Respostas possíveis:**
| Status | Descrição |
|---------|------------|
| `200 OK` | Lista ordenada de horários |
| `400 Bad Request` | Parâmetro `day` ausente ou inválido |
| `404 Not Found` | Schedule inexistente |

---

### **5️⃣ Excluir Schedule**
`DELETE /api/schedules/{id}`

- Exclui o Schedule e todos os TimeSlots relacionados (cascade).
- Retorna `204 No Content` se sucesso.

---

## ✅ Testes Realizados (Postman)

| Caso | Descrição | Resultado |
|------|------------|------------|
| A | `poloId` inválido (não POLO) | **400 Bad Request** ✅ |
| B | `subjectId` inexistente | **404 Not Found** ✅ |
| C | Período sobreposto | **400 Bad Request** ✅ |
| D | Criação válida | **201 Created** ✅ |
| E | Geração bulk padrão | **201 Created** ✅ |
| F | Consulta por dia `mon` | **200 OK** ✅ |
| G | Consulta por `sat` | **200 OK** ✅ |
| H | Exclusão de schedule | **204 No Content** ✅ |
| I | Cascade delete verificado (`count=0`) | ✅ |

