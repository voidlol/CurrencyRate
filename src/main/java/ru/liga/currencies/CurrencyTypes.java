package ru.liga.currencies;

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
}
