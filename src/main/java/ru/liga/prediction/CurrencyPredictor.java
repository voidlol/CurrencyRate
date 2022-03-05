package ru.liga.prediction;

import ru.liga.currencies.CurrencyRate;

import java.time.LocalDate;
import java.util.List;

public interface CurrencyPredictor {

    /**
     * Predicts rates
     * @param data currency rates to build predict based on
     * @param days range for prediction
     * @return List of strings with predicted rates
     */
    List<CurrencyRate> predict(List<CurrencyRate> data, LocalDate targetDate, boolean isRange);
    int getRequiredDataSize();
}
