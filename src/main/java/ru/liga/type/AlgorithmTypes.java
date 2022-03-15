package ru.liga.type;

import lombok.extern.slf4j.Slf4j;
import ru.liga.algorithm.*;

import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
public enum AlgorithmTypes {
    ACTUAL(new ActualAlgorithm()),
    LINEAR(new LinearAlgorithm()),
    MYSTIC(new MysticAlgorithm()),
    MEAN(new ArithmeticMean());

    private final CurrencyForecaster algorithm;

    AlgorithmTypes(CurrencyForecaster algorithm) {
        this.algorithm = algorithm;
    }

    public static String getString() {
        return Arrays.stream(values())
                .map(Enum::name)
                .collect(Collectors.joining(" | ", "<", ">"));
    }

    public CurrencyForecaster getAlgorithm() {
        return algorithm;
    }

}
