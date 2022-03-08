package ru.liga.repository;

import ru.liga.currencies.CurrencyRate;
import ru.liga.currencies.CurrencyTypes;

import java.time.LocalDate;
import java.util.List;

public interface CurrencyRepository {

    void addAll(CurrencyTypes type);
    List<CurrencyRate> getRates(CurrencyTypes type, int amount);
    List<CurrencyRate> getAllRates(CurrencyTypes type);
    CurrencyRate getRateForDate(CurrencyTypes type, LocalDate date);
}
