package ru.liga.algorithm;

import ru.liga.currency.CurrencyRate;
import ru.liga.input.Period;
import ru.liga.type.CurrencyTypes;
import ru.liga.repository.CurrencyRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Predicts rates based on average rate for last 7 days
 */

public class ArithmeticMean implements CurrencyForecaster {

    private static final int AVERAGE_FOR_DAYS = 7;

    @Override
    public List<CurrencyRate> getForecast(CurrencyRepository repository, CurrencyTypes type, Period period) {
        List<CurrencyRate> result = new LinkedList<>();
        List<CurrencyRate> lastWeekRates = repository.getRates(type, AVERAGE_FOR_DAYS);
        while (!lastWeekRates.get(0).getDate().isEqual(period.getTargetDate())) {
            LocalDate nextDay = lastWeekRates.get(0).getDate().plusDays(1);
            Double averageRate = lastWeekRates.stream()
                    .limit(AVERAGE_FOR_DAYS)
                    .mapToDouble(CurrencyRate::getRate)
                    .average()
                    .orElse(Double.NaN);
            lastWeekRates.add(0, new CurrencyRate(nextDay, type, averageRate));
        }
        long daysToShow = period.isRange() ? ChronoUnit.DAYS.between(LocalDate.now(), period.getTargetDate()) : 1L;
        lastWeekRates.stream()
                .limit(daysToShow)
                .forEach(result::add);
        Collections.reverse(result);
        return result;
    }

}
