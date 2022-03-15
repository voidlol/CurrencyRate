package ru.liga.type;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
public enum CurrencyTypes {
    USD,
    EUR,
    TRY,
    AMD,
    BGN;

    public static String getString() {
        return Arrays.stream(values()).map(Enum::name).collect(Collectors.joining(" | ", "<", ">"));
    }
}
