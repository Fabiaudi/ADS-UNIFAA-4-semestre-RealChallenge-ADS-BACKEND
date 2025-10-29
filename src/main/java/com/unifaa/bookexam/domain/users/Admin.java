package com.unifaa.bookexam.domain.users;

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
