package com.unifaa.bookexam.domain.users;

import jakarta.persistence.*;
import lombok.*;

@Entity
@DiscriminatorValue("STUDENT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Student extends User {
    
    private String course;

    @ManyToOne
    @JoinColumn(name = "student_polo_id")
    private Polo studentPolo;
}
