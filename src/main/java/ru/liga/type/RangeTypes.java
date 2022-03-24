package ru.liga.type;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Getter
public enum RangeTypes {
    WEEK(7), MONTH(30);

    private final int days;

    public static String getString() {
        return Arrays.stream(values())
                .map(c -> c.name().toLowerCase())
                .collect(Collectors.joining(" | ", "<", ">"));
    }
}
