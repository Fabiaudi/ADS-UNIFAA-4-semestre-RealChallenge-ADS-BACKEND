package com.unifaa.bookexam.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@DiscriminatorValue("ADMIN")
@Getter
@Setter
@NoArgsConstructor
// @AllArgsConstructor
public class Admin extends User {
    // Seus atributos são herdados do User
}
