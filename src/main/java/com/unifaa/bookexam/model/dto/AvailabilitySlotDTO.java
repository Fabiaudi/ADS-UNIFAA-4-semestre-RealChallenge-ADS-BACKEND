package com.unifaa.bookexam.model.dto;

import java.time.LocalTime;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AvailabilitySlotDTO {

    private String poloId;
    private UUID subjectId;
    private String date;
    private LocalTime time;
    private int capacity; // capacidade do polo
    private int booked; // já reservado
    private int available; // capacity - reservado
    private boolean isAvailable;
}
