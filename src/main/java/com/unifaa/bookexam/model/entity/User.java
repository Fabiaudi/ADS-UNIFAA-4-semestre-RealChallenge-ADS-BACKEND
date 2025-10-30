package com.unifaa.bookexam.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entidade JPA abstrata base para todos os usuários (Admin, Student, Polo).
 * Utiliza estratégia de herança SINGLE_TABLE com uma coluna discriminadora "type".
 */
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class User {

	/**
     * ID do usuário (Matrícula). Ex: A00001, P00001, E00001.
     */
    @Id
    private String id;  
	
	/**
     * Nome completo do usuário.
     */	
    private String name;
	
	/**
     * Email único do usuário (usado para login).
     */
    private String email;

	/**
     * Hash da senha (armazenado no formato BCrypt).
     */
    @Column(name = "password_hash")
    private String passwordHash;

	/**
     * Timestamp de criação do usuário (gerenciado pelo banco).
     */
    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Tipo do usuário (Coluna Discriminadora).
     * Ex: ADMIN, STUDENT, POLO.
     * (Definido como insertable=false, updatable=false pois é gerenciado pelo JPA
     * via @DiscriminatorValue).
     */
    @Column(name = "type", insertable = false, updatable = false)
    private String type;
}