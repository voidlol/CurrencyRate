package ru.liga.currencies;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum CurrencyTypes {
    USD, EUR, TRY;

    public static CurrencyTypes findByName(String name) {
        for (CurrencyTypes currencyTypes : values()) {
            if (currencyTypes.name().equalsIgnoreCase(name)) {
                return currencyTypes;
            }
        }

        return null;
    }

    public static String getString() {
        return Arrays.stream(values()).map(Enum::name).collect(Collectors.joining(" | ", "<", ">"));
    }
}
