package ru.liga.currency;
import org.junit.jupiter.api.Test;
import ru.liga.type.CurrencyTypes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

class CurrencyRateTest {

    @Test
    void testToString() {
        CurrencyRate rate = new CurrencyRate(LocalDate.parse("21.02.2022", DateTimeFormatter.ofPattern("dd.MM.yyyy")), CurrencyTypes.EUR, 87.5);
        assertThat(rate.toString()).hasToString("Пн 21.02.2022 - 87,50");
    }
}