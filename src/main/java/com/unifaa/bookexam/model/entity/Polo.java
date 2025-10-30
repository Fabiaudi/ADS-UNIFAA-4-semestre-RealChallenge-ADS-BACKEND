package com.unifaa.bookexam.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@DiscriminatorValue("POLO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Polo extends User {
    
    private String location;
    private Integer availability;
}
