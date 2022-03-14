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

    public static CurrencyTypes findByName(String name) {
        log.debug("Trying to find currency with name {}", name);
        for (CurrencyTypes currencyTypes : values()) {
            if (currencyTypes.name().equalsIgnoreCase(name)) {
                log.debug("Found currency with name {}: {}", name, currencyTypes);
                return currencyTypes;
            }
        }
        log.debug("No such currency with name {}", name);
        return null;
    }

}
