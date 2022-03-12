package ru.liga.type;


import java.util.Arrays;
import java.util.stream.Collectors;

public enum RangeTypes {
    WEEK(7), MONTH(30);

    private final int days;

    RangeTypes(int days) {
        this.days = days;
    }

    public static RangeTypes findByName(String s) {
        for (RangeTypes rangeTypes : values()) {
            if (rangeTypes.name().equalsIgnoreCase(s)) {
                return rangeTypes;
            }
        }
        return null;
    }

    public int getDays() {
        return days;
    }

    public static String getString() {
        return Arrays.stream(values())
                .map(c -> c.name().toLowerCase())
                .collect(Collectors.joining(" | ", "<", ">"));
    }

}