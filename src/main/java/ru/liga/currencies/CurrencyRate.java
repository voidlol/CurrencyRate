package ru.liga.currencies;

import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Objects;

@Getter
public class CurrencyRate {

    private final LocalDate date;
    private final CurrencyTypes type;
    private final Double rate;

    public CurrencyRate(LocalDate date, CurrencyTypes type, Double rate) {
        this.date = date;
        this.type = type;
        this.rate = rate;
    }

    /**
     * Rounds rate to 2 decimals.
     * Using 100 because it's 10^2, so we can round up to 2 decimals :)
     * @return rounded rate to 2 decimals
     */
    private Double getRoundedRate() {
        return Math.round(rate * 100) / 100d;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrencyRate that = (CurrencyRate) o;
        return Objects.equals(getDate(), that.getDate()) && getType() == that.getType() && Objects.equals(getRoundedRate(), that.getRoundedRate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDate(), getType(), getRate());
    }

    @Override
    public String toString() {
        String dayName = date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.forLanguageTag("ru"));
        return String.format("%s %s - %.2f", dayName, date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")), getRoundedRate());
    }
}
