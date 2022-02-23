package ru.liga.currencies;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

public class CurrencyRate {

    private LocalDate date;
    private CurrencyTypes type;
    private Double rate;

    public CurrencyRate(LocalDate date, CurrencyTypes type, Double rate) {
        this.date = date;
        this.type = type;
        this.rate = rate;
    }

    public LocalDate getDate() {
        return date;
    }

    public CurrencyTypes getType() {
        return type;
    }

    public Double getRate() {
        return rate;
    }

    @Override
    public String toString() {
        String dayName = date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.forLanguageTag("ru"));
        Double roundedRate = Math.round(rate * 100) / 100d;
        return String.format("%s %s - %.2f", dayName, date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")), roundedRate);
    }
}
