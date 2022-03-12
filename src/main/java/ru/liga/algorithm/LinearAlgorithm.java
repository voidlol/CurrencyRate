package ru.liga.algorithm;

import ru.liga.currency.CurrencyRate;
import ru.liga.type.CurrencyTypes;
import ru.liga.repository.CurrencyRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class LinearAlgorithm implements CurrencyForecaster {

    private static final int DAYS_AMOUNT_TO_INTERPOLATE = 30;

    @Override
    public List<CurrencyRate> getForecast(CurrencyRepository repository, CurrencyTypes type, LocalDate targetDate, boolean isRange) {
        List<CurrencyRate> data = repository.getRates(type, DAYS_AMOUNT_TO_INTERPOLATE);
        LocalDate nextDay = data.get(0).getDate().plusDays(1);
        data.sort(Comparator.comparing(CurrencyRate::getDate));
        double[] dates = new double[data.size()];
        double[] rates = new double[data.size()];
        for (int i = 0; i < data.size(); i++) {
            dates[i] = i + 1d;
            rates[i] = data.get(i).getRate();
        }
        LinearRegression linearRegression = new LinearRegression(dates, rates);
        List<CurrencyRate> result = new ArrayList<>();
        int i = 1;
        do {
            if (nextDay.isAfter(LocalDate.now())) {
                result.add(new CurrencyRate(nextDay, type, linearRegression.predict(dates[data.size() - 1] + i)));
            }
            nextDay = nextDay.plusDays(1);
            i++;
        } while (!nextDay.isAfter(targetDate));

        if (isRange) {
            return result;
        } else {
            Collections.reverse(result);
            return result.stream().limit(1).collect(Collectors.toList());
        }
    }
}
