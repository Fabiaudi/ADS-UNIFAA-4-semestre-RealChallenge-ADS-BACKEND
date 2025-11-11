package com.unifaa.bookexam.model.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.unifaa.bookexam.model.dto.BookingRequestDTO;
import com.unifaa.bookexam.model.dto.BookingResponseDTO;
import com.unifaa.bookexam.model.entity.Booking;

@Mapper(componentModel = "spring")
public interface BookingsMappers {

    @Mapping(source = "studentId", target = "studentId")
    @Mapping(source = "subjectId", target = "subject.id") 
    @Mapping(source = "poloId", target = "polo.id") // popula booking.polo.id
    @Mapping(source = "date", target = "date")

    Booking toEntity(BookingRequestDTO dto);

    @Mapping(source = "polo.id", target = "poloId")
    @Mapping(source = "subject.id", target = "subjectId")
    @Mapping(source = "studentId", target = "studentId")
    @Mapping(source = "date", target = "date")
    @Mapping(source = "time", target = "time")
    BookingRequestDTO requestToDTO(Booking booking);

    // Aqui mudamos para retornar BookingResponseDTO
    @Mapping(source = "id", target = "id")
    @Mapping(source = "studentId", target = "studentId")
    @Mapping(source = "subject.id", target = "subjectId")
    @Mapping(source = "polo.id", target = "poloId") // devolve só o ID no DTO
    @Mapping(source = "date", target = "date")
    BookingResponseDTO toDTO(Booking booking);

}
