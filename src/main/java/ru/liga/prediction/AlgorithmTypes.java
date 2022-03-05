package ru.liga.prediction;

public enum AlgorithmTypes {
    ACTUAL(new ArithmeticMean()), LINEAR(new LinearAlgorithm()), MYSTIC(null);

    private final CurrencyPredictor algorithm;

    AlgorithmTypes(CurrencyPredictor algorithm) {
        this.algorithm = algorithm;
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
