package ru.liga.algorithm;

import ru.liga.currency.CurrencyRate;
import ru.liga.type.CurrencyTypes;
import ru.liga.repository.CurrencyRepository;

import java.time.LocalDate;
import java.util.List;

public interface CurrencyForecaster {

    List<CurrencyRate> getForecast(CurrencyRepository repository, CurrencyTypes type, LocalDate targetDate, boolean isRange);
}
