package com.unifaa.bookexam.config;

import com.unifaa.bookexam.model.enums.DayOfWeek;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Converte o enum DayOfWeek <-> String do banco.
 * Persistimos exatamente 'mon','tue','wed','thu','fri','sat' (VARCHAR(3)).
 *
 * Uso:
 * - Em campos de entidade: @Convert(converter = DayOfWeekConverter.class)
 * - Não está auto-aplicado para evitar interferir em outros mapeamentos.
 */
@Converter(autoApply = false)
public class DayOfWeekConverter implements AttributeConverter<DayOfWeek, String> {

    @Override
    public String convertToDatabaseColumn(DayOfWeek attribute) {
        return attribute == null ? null : attribute.code();
    }

    @Override
    public DayOfWeek convertToEntityAttribute(String dbData) {
        if (dbData == null)
            return null;
        return DayOfWeek.from(dbData);
    }
}
