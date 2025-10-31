package com.unifaa.bookexam.model.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.unifaa.bookexam.model.dto.BookingRequestDTO;
import com.unifaa.bookexam.model.dto.BookingResponseDTO;
import com.unifaa.bookexam.model.entity.Booking;

@Mapper(componentModel = "spring")
public interface BookingsMappers {

    @Mapping(source = "studentId", target = "studentId")
    @Mapping(source = "subjectId", target = "subjectId")
    @Mapping(source = "poloId", target = "poloId")
    @Mapping(source = "date", target = "date")

    Booking toEntity(BookingRequestDTO dto);

    BookingRequestDTO requestToDTO(Booking booking);

    // Aqui mudamos para retornar BookingResponseDTO
    @Mapping(source = "bookingId", target = "id")
    @Mapping(source = "studentId", target = "studentId")
    @Mapping(source = "subjectId", target = "subjectId")
    @Mapping(source = "poloId", target = "poloId")
    @Mapping(source = "date", target = "date")
    BookingResponseDTO toDTO(Booking booking);

}
