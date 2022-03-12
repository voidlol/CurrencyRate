package ru.liga.data;

import ru.liga.currency.CurrencyRate;
import ru.liga.type.CurrencyTypes;

import java.util.List;

public interface CurrencyParser {
    /**
     * Reads currency data from source
     * @param type type of currency to read
     * @return list of currency rates read from source
     */
    List<CurrencyRate> getCurrencyRates(CurrencyTypes type);
}
