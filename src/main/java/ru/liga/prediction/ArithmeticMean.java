package ru.liga.prediction;

import ru.liga.currencies.CurrencyRate;
import ru.liga.currencies.CurrencyTypes;

import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Predicts rates based on average rate for last 7 days
 */

public class ArithmeticMean implements CurrencyPredictor {

    private static final int AVERAGE_FOR_DAYS = 7;

    @Override
    public List<CurrencyRate> predict(List<CurrencyRate> data, int days) {
        List<CurrencyRate> result = new LinkedList<>();
        data.sort((o1, o2) -> o2.getDate().compareTo(o1.getDate()));
        CurrencyTypes type = data.get(0).getType();
        List<CurrencyRate> lastWeekRates = data.stream().limit(AVERAGE_FOR_DAYS).collect(Collectors.toList());
        while (!lastWeekRates.get(0).getDate().isEqual(LocalDate.now().plusDays(days))) {
            LocalDate nextDay = lastWeekRates.get(0).getDate().plusDays(1);
            Double averageRate = lastWeekRates.stream().mapToDouble(CurrencyRate::getRate).average().orElse(Double.NaN);
            lastWeekRates.add(0, new CurrencyRate(nextDay, type, averageRate));
            lastWeekRates.remove(lastWeekRates.size() - 1);
        }
        lastWeekRates.stream().limit(days).forEach(result::add);
        Collections.reverse(result);
        return result;
    }

    @Override
    public int getRequiredDataSize() {
        return AVERAGE_FOR_DAYS;
    }
}
