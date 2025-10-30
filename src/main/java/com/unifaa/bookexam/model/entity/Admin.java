package com.unifaa.bookexam.model.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidade que representa um Administrador.
 * Herda todos os campos da classe {@link User}.
 * Mapeada com o valor "ADMIN" na coluna discriminadora.
 */
@Entity
@DiscriminatorValue("ADMIN")
@Getter
@Setter
@NoArgsConstructor
// @AllArgsConstructor // (Removido pois Admin não tem campos próprios)
public class Admin extends User {
    // Seus atributos são herdados do User
}
