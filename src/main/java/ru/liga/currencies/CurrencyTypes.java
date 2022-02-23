package ru.liga.currencies;

import java.util.HashSet;
import java.util.Set;

public enum CurrencyTypes {
    USD, EUR, TRY;


    /**
     * Creates a set of valid currency names
     * @return set of valid currency names
     */

    public static Set<String> getValidCurrencies() {
        Set<String> result = new HashSet<>();
        for (CurrencyTypes c : CurrencyTypes.values()) {
            result.add(c.name());
        }
        return result;
    }
}
