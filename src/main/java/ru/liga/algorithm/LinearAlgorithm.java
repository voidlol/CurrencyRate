package ru.liga.algorithm;

import ru.liga.currency.CurrencyRate;
import ru.liga.input.Period;
import ru.liga.type.CurrencyTypes;
import ru.liga.repository.CurrencyRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LinearAlgorithm implements CurrencyForecaster {

    private static final int DAYS_AMOUNT_TO_INTERPOLATE = 30;
    private int meanNominal;

    @Override
    public List<CurrencyRate> getForecast(CurrencyRepository repository, CurrencyTypes type, Period period) {
        List<CurrencyRate> data = repository.getRates(type, DAYS_AMOUNT_TO_INTERPOLATE);
        meanNominal = (int) data.stream()
                .mapToInt(CurrencyRate::getNominal)
                .average()
                .orElse(1);
        LocalDate nextDay = data.get(0).getDate().plusDays(1);
        data.sort(Comparator.comparing(CurrencyRate::getDate));
        double[] dates = new double[data.size()];
        double[] rates = new double[data.size()];
        for (int i = 0; i < data.size(); i++) {
            dates[i] = i + 1d;
            rates[i] = data.get(i).getRate();
        }
        LinearRegression linearRegression = new LinearRegression(dates, rates);
        if (!period.isRange()) {
            return Collections.singletonList(
                    getRateForDate(period.getTargetDate(), data.get(DAYS_AMOUNT_TO_INTERPOLATE - 1).getDate(), type, linearRegression));
        }
        List<CurrencyRate> result = new ArrayList<>();
        int i = 1;
        do {
            if (nextDay.isAfter(LocalDate.now())) {
                result.add(new CurrencyRate(nextDay, type, linearRegression.predict(dates[data.size() - 1] + i)));
            }
            nextDay = nextDay.plusDays(1);
            i++;
        } while (!nextDay.isAfter(period.getTargetDate()));

        return result;
    }


    private CurrencyRate getRateForDate(LocalDate targetDate, LocalDate lastDateInSource, CurrencyTypes type, LinearRegression regression) {
        long daysBetween = ChronoUnit.DAYS.between(lastDateInSource, targetDate);
        return new CurrencyRate(targetDate, type, regression.predict(DAYS_AMOUNT_TO_INTERPOLATE + daysBetween) * meanNominal);
    }
}
