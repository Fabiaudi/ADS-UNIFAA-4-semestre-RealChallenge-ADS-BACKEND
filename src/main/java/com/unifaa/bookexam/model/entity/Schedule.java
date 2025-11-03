package com.unifaa.bookexam.model.entity;

import java.time.LocalDate;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Representa uma janela de oferta de agendamento (Schedule).
 * 
 * Cada Schedule pertence a um Polo (via poloId)
 * e cobre uma disciplina (via subjectId),
 * entre as datas startDate e endDate.
 *
 * 🔹 Alinhado ao diagrama de classes e V1__init.sql.
 * 🔹 Usa apenas IDs (sem @ManyToOne) para manter desacoplado.
 * 🔹 TimeSlots caem em cascata (mapeado do lado deles).
 */
@Entity
@Table(name = "schedules")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
public class Schedule {

    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID id;

    // ID do Polo (usuário do tipo POLO)
    @Column(name = "polo_id", nullable = false, length = 255)
    private String poloId;

    // ID da disciplina (Subject)
    @Column(name = "subject_id", nullable = false)
    private UUID subjectId;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;
}
