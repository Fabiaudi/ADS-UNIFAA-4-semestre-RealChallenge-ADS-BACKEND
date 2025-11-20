package com.unifaa.bookexam.model.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.unifaa.bookexam.model.enums.BookingStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingResponseDTO {

    private String id;
    private String studentName;
    private String subjectName;
    private String poloName;
    private LocalDate date;
    private LocalTime time;
    private BookingStatus status;
}
