package com.unifaa.bookexam.model.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidade que representa um Aluno (Student).
 * Herda de {@link User} e adiciona campos específicos do aluno.
 * Mapeada com o valor "STUDENT" na coluna discriminadora.
 */
@Entity
@DiscriminatorValue("STUDENT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Student extends User {
    
	/**
     * Curso ao qual o aluno pertence (ex: "ADS").
     */
    private String course;
	
	/**
     * Relacionamento ManyToOne com o Polo ao qual o aluno está vinculado.
     */
    @ManyToOne
    @JoinColumn(name = "student_polo_id")
    private Polo studentPolo;
}
