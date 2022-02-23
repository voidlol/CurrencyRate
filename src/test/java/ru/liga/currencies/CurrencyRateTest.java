package ru.liga.currencies;

import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.*;

public class CurrencyRateTest {

    @Test
    public void testToString() {
        CurrencyRate rate = new CurrencyRate(LocalDate.parse("21.02.2022", DateTimeFormatter.ofPattern("dd.MM.yyyy")), CurrencyTypes.EUR, 87.5);
        assertEquals("Пн 21.02.2022 - 87,50", rate.toString());
    }
}