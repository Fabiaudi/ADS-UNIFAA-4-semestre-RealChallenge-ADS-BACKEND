package com.unifaa.bookexam.model.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.unifaa.bookexam.model.entity.Subject;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AvailabilitySlotDTO {

    private String poloId;
    private Subject subject;
    private LocalDate date;
    private LocalTime time;
    private int capacity; // capacidade do polo
    private int booked; // já reservado
    private int available; // capacity - reservado
    private boolean hasAvailability;
}
