package ru.liga.input;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@EqualsAndHashCode
public class Period {

    private final LocalDate targetDate;
    private final boolean isRange;

    public Period(LocalDate targetDate, boolean isRange) {
        this.targetDate = targetDate;
        this.isRange = isRange;
    }
}
