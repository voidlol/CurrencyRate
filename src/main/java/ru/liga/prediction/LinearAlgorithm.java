package ru.liga.prediction;

import ru.liga.currencies.CurrencyRate;
import ru.liga.currencies.CurrencyTypes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class LinearAlgorithm implements CurrencyPredictor {

    @Override
    public List<CurrencyRate> predict(List<CurrencyRate> data, LocalDate targetDate, boolean isRange) {
        data.sort(Comparator.comparing(CurrencyRate::getDate));
        double[] dates = new double[data.size()];
        double[] rates = new double[data.size()];
        for (int i = 0; i < data.size(); i++) {
            dates[i] = i + 1d;
            rates[i] = data.get(i).getRate();
        }
        LinearRegression linearRegression = new LinearRegression(dates, rates);
        List<CurrencyRate> result = new ArrayList<>();
        CurrencyTypes currencyTypes = data.get(0).getType();
        int i = 1;
        do {
            result.add(new CurrencyRate(LocalDate.now().plusDays(i), currencyTypes, linearRegression.predict(dates[data.size() - 1] + i)));
            i++;
        } while (!result.get(result.size() - 1).getDate().isEqual(targetDate));

        if (isRange) {
            return result;
        } else {
            Collections.reverse(result);
            return result.stream().limit(1).collect(Collectors.toList());
        }
    }

    @Override
    public int getRequiredDataSize() {
        return 30;
    }
}
