package ru.liga.prediction;

import ru.liga.currencies.CurrencyRate;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.time.LocalDate;
import java.util.List;

public class ActualAlgorithm implements CurrencyPredictor {

    @Override
    public List<CurrencyRate> predict(List<CurrencyRate> data, LocalDate targetDate, boolean isRange) {
        throw new NotImplementedException();
    }

    @Override
    public int getRequiredDataSize() {
        return 0;
    }
}
