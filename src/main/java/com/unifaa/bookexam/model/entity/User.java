package com.unifaa.bookexam.model.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidade base de usuário (SINGLE_TABLE).
 * Coluna discriminadora 'type' armazena ADMIN | POLO | STUDENT.
 */
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public abstract class User {

    /** Matrícula: A00001, P00001, E00001 */
    @Id
    private String id;

    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    /** hash BCrypt vindo da coluna password_hash */
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    /** Coluna discriminadora — gerenciada pelo JPA */
    @Column(name = "type", insertable = false, updatable = false)
    private String type;
}
