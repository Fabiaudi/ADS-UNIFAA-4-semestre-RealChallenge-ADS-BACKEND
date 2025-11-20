package com.unifaa.bookexam.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Dia da semana usado em TimeSlot.
 * No banco: VARCHAR(3) com valores 'mon','tue','wed','thu','fri','sat'.
 *
 * Observação:
 * - @JsonValue para serializar como 'mon' no JSON.
 * - from(String) é tolerante com maiúsculas/minúsculas.
 *
 * IMPORTANTE:
 * - Para persistência JPA exatamente como 'mon'..'sat',
 *   no próximo passo criaremos um AttributeConverter
 *   (DayOfWeekConverter) mapeando enum <-> String.
 */
public enum DayOfWeek {
    MON("mon"),
    TUE("tue"),
    WED("wed"),
    THU("thu"),
    FRI("fri"),
    SAT("sat");

    private final String code;

    DayOfWeek(String code) {
        this.code = code;
    }

    /** Serializa para JSON como 'mon', 'tue'... */
    @JsonValue
    public String code() {
        return code;
    }

    /** Constrói a partir de 'mon','tue'... (case-insensitive). */
    @JsonCreator
    public static DayOfWeek from(String value) {
        if (value == null) return null;
        String v = value.trim().toLowerCase();
        for (DayOfWeek d : values()) {
            if (d.code.equals(v)) return d;
        }
        throw new IllegalArgumentException("Invalid day: " + value + " (expected: mon|tue|wed|thu|fri|sat)");
    }

    public static DayOfWeek fromJava(java.time.DayOfWeek javaDow) {
    return switch (javaDow) {
        case MONDAY -> MON;
        case TUESDAY -> TUE;
        case WEDNESDAY -> WED;
        case THURSDAY -> THU;
        case FRIDAY -> FRI;
        case SATURDAY -> SAT;
        case SUNDAY -> throw new IllegalArgumentException("Domingo não permitido"); 
    };
}

}
