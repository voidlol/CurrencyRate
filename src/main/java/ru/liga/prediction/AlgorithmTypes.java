package ru.liga.prediction;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum AlgorithmTypes {
    ACTUAL(new ActualAlgorithm()), LINEAR(new LinearAlgorithm()), MYSTIC(new ArithmeticMean());

    private final CurrencyPredictor algorithm;

    AlgorithmTypes(CurrencyPredictor algorithm) {
        this.algorithm = algorithm;
    }

    public static String getString() {
        return Arrays.stream(values()).map(Enum::name).collect(Collectors.joining(" | ", "<", ">"));
    }

    public CurrencyPredictor getAlgorithm() {
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
