package ru.liga.repository;

import ru.liga.currencies.CurrencyRate;
import ru.liga.currencies.CurrencyTypes;
import ru.liga.data.CurrencyParser;

import java.time.LocalDate;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InMemoryCurrencyRepository implements CurrencyRepository {

    private final Map<CurrencyTypes, List<CurrencyRate>> data = new EnumMap<>(CurrencyTypes.class);
    private final CurrencyParser parser;

    public InMemoryCurrencyRepository(CurrencyParser parser) {
        this.parser = parser;
    }

    @Override
    public void addAll(CurrencyTypes type) {
        if (data.containsKey(type)) {
            data.get(type).addAll(parser.getCurrencyRates(type));
        } else {
            data.put(type, parser.getCurrencyRates(type));
        }
    }

    @Override
    public List<CurrencyRate> getRates(CurrencyTypes type, int amount) {
        if (!data.containsKey(type)) {
            addAll(type);
        }
        data.get(type).sort((currencyRate1, currencyRate2) -> currencyRate2.getDate().compareTo(currencyRate1.getDate()));
        return data.get(type).stream()
                .limit(amount)
                .collect(Collectors.toList());
    }

    @Override
    public List<CurrencyRate> getAllRates(CurrencyTypes type) {
        if (!data.containsKey(type)) {
            addAll(type);
        }
        data.get(type).sort((currencyRate1, currencyRate2) -> currencyRate2.getDate().compareTo(currencyRate1.getDate()));
        return data.get(type);
    }

    @Override
    public CurrencyRate getRateForDate(CurrencyTypes type, LocalDate date) {
        if (!data.containsKey(type)) {
            addAll(type);
        }
        return data.get(type).stream()
                .filter(currencyRate -> isDateEqualOrBefore(currencyRate.getDate(), date))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    private boolean isDateEqualOrBefore(LocalDate date, LocalDate targetDate) {
        return date.isEqual(targetDate) || date.isBefore(targetDate);
    }
}
