package ru.liga.repository;

import ru.liga.currency.CurrencyRate;
import ru.liga.type.CurrencyTypes;

import java.time.LocalDate;
import java.util.List;

public interface CurrencyRepository {

    void addAll(CurrencyTypes type);
    List<CurrencyRate> getRates(CurrencyTypes type, int amount);
    List<CurrencyRate> getAllRates(CurrencyTypes type);
    CurrencyRate getRateForDate(CurrencyTypes type, LocalDate date);
}
