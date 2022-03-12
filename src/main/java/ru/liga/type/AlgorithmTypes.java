package ru.liga.type;

import ru.liga.algorithm.ActualAlgorithm;
import ru.liga.algorithm.CurrencyForecaster;
import ru.liga.algorithm.LinearAlgorithm;
import ru.liga.algorithm.MysticAlgorithm;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum AlgorithmTypes {
    ACTUAL(new ActualAlgorithm()), LINEAR(new LinearAlgorithm()), MYSTIC(new MysticAlgorithm());

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

    public static AlgorithmTypes findByName(String name) {
        for (AlgorithmTypes algorithmTypes : values()) {
            if (algorithmTypes.name().equalsIgnoreCase(name)) {
                return algorithmTypes;
            }
        }
        return null;
    }
}
