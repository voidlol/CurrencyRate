package ru.liga.type;


import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
public enum RangeTypes {
    WEEK(7), MONTH(30);

    private final int days;

    RangeTypes(int days) {
        this.days = days;
    }


    public int getDays() {
        return days;
    }

    public static String getString() {
        return Arrays.stream(values())
                .map(c -> c.name().toLowerCase())
                .collect(Collectors.joining(" | ", "<", ">"));
    }

    public static RangeTypes findByName(String name) {
        log.debug("Trying to find range with name {}", name);
        for (RangeTypes rangeTypes : values()) {
            if (rangeTypes.name().equalsIgnoreCase(name)) {
                log.debug("Found range with name {}: {}", name, rangeTypes);
                return rangeTypes;
            }
        }
        log.debug("No such range with name {}", name);
        return null;
    }
}
