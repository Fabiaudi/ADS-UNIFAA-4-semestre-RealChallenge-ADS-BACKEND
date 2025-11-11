package com.unifaa.bookexam.model.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingRequestDTO {

    private String subjectId;
    private String studentId;
    private String poloId;
    private LocalDate date;
    private LocalTime time;
}
