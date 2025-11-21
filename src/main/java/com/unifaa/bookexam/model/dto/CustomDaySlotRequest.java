package com.unifaa.bookexam.model.dto;

public record CustomDaySlotRequest(
        String day, // "mon", "tue", "wed", "thu", "fri", "sat"
        String start, // "14:00"
        String end // "19:00"
) {
}
