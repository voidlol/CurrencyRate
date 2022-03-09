package ru.liga.prediction;

import ru.liga.currencies.CurrencyRate;
import ru.liga.currencies.CurrencyTypes;
import ru.liga.repository.CurrencyRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MysticAlgorithm implements CurrencyPredictor {

    private final List<LocalDate> fullMoonList = new ArrayList<>();

    public MysticAlgorithm() {
        fullMoonList.add(LocalDate.of(2022, 2, 16));
        fullMoonList.add(LocalDate.of(2022, 1, 18));
        fullMoonList.add(LocalDate.of(2021, 12, 19));
    }

    @Override
    public List<CurrencyRate> getForecast(CurrencyRepository repository, CurrencyTypes type, LocalDate targetDate, boolean isRange) {
        List<CurrencyRate> fullMoonRates = new ArrayList<>();
        fullMoonList.forEach(d -> fullMoonRates.add(repository.getRateForDate(type, d)));

        Double averageRate = fullMoonRates.stream()
                .mapToDouble(CurrencyRate::getRate)
                .average().orElse(0D);

        List<CurrencyRate> result = new ArrayList<>();
        if (isRange) {
            CurrencyRate lastRate = new CurrencyRate(LocalDate.now().plusDays(1), type, averageRate);
            result.add(lastRate);
            while (!lastRate.getDate().isEqual(targetDate)) {
                Double newRate = getNewRate(lastRate.getRate());
                lastRate = new CurrencyRate(lastRate.getDate().plusDays(1), type, newRate);
                result.add(lastRate);
            }
        } else {
            result.add(new CurrencyRate(targetDate, type, averageRate));
        }
        return result;
    }

    private Double getNewRate(Double initialRate) {
        Random random = new Random();
        int min = -10;
        int max = 10;
        int diff = max - min;
        int randValue = min + random.nextInt(diff + 1);

        double percentage = randValue / 100D;
        return (1 + percentage) * initialRate;
    }

}
