package ru.liga.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.liga.algorithm.*;

import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Getter
public enum AlgorithmTypes {
    ACTUAL(new ActualAlgorithm()),
    LINEAR(new LinearAlgorithm()),
    MYSTIC(new MysticAlgorithm()),
    MEAN(new ArithmeticMean());

    private final CurrencyForecaster algorithm;

    public static String getString() {
        return Arrays.stream(values())
                .map(Enum::name)
                .collect(Collectors.joining(" | ", "<", ">"));
    }

}
