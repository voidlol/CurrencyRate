package ru.liga.currency;

import lombok.*;
import ru.liga.type.CurrencyTypes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

@Getter
@EqualsAndHashCode
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class CurrencyRate {

    @NonNull
    private LocalDate date;
    @NonNull
    private CurrencyTypes type;
    @NonNull
    private Double rate;

    private Integer nominal;

    public void normalize() {
        this.rate /= this.nominal;
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
    public String toString() {
        String dayName = date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.forLanguageTag("ru"));
        return String.format("%s %s - %.2f", dayName, date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")), getRoundedRate());
    }
}
