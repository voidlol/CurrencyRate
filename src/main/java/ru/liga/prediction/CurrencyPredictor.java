package ru.liga.prediction;

import ru.liga.currencies.CurrencyRate;
import ru.liga.currencies.CurrencyTypes;
import ru.liga.repository.CurrencyRepository;

import java.time.LocalDate;
import java.util.List;

public interface CurrencyPredictor {

    List<CurrencyRate> getForecast(CurrencyRepository repository, CurrencyTypes type, LocalDate targetDate, boolean isRange);
}
