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
    private String studentId;
    private String subjectId;
    private String poloId;
    private LocalDate date;
    private LocalTime time;
    private BookingStatus status;
}
