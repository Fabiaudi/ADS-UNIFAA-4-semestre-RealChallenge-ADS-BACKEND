package com.unifaa.bookexam.model.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidade que representa um Polo (localidade/campus).
 * Herda de {@link User} e adiciona campos específicos do polo.
 * Mapeada com o valor "POLO" na coluna discriminadora.
 */
@Entity
@DiscriminatorValue("POLO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Polo extends User {
    
	/**
     * Localização do Polo (ex: "Barra do Piraí").
     */
    private String location;
	
	/**
     * Disponibilidade (vagas) simultâneas que o polo oferece.
     */
    private Integer availability;
}
