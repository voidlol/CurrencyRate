package ru.liga.type;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum OutputType {
    GRAPH, LIST;

    public static String getString() {
        return Arrays.stream(values())
                .map(Enum::name)
                .collect(Collectors.joining(" | ", "<", ">"));

    }
}
