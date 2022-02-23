package ru.liga.controllers;

import org.junit.Test;
import ru.liga.currencies.CurrencyRate;
import ru.liga.currencies.CurrencyTypes;
import ru.liga.data.CurrencyParser;
import ru.liga.prediction.ArithmeticMean;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class UserInputControllerTest {

    private static final List<CurrencyRate> dataEUR = new ArrayList<>();
    private static final List<CurrencyRate> dataUSD = new ArrayList<>();
    private static final List<CurrencyRate> dataTRY = new ArrayList<>();

    private static final String TOMORROW = getFormattedDateString(LocalDate.now().plusDays(1));
    private static final String THE_DAY_AFTER_TOMORROW = getFormattedDateString(LocalDate.now().plusDays(2));
    private static final String THIRD_DAY = getFormattedDateString(LocalDate.now().plusDays(3));
    private static final String FOURTH_DAY = getFormattedDateString(LocalDate.now().plusDays(4));
    private static final String FIFTH_DAY = getFormattedDateString(LocalDate.now().plusDays(5));
    private static final String SIXTH_DAY = getFormattedDateString(LocalDate.now().plusDays(6));
    private static final String SEVENTH_DAY = getFormattedDateString(LocalDate.now().plusDays(7));

    static class StubParser implements CurrencyParser {
        @Override
        public List<CurrencyRate> getCurrencyRates(CurrencyTypes type) {
            switch (type) {
                case EUR: return dataEUR;
                case USD: return dataUSD;
                case TRY: return dataTRY;
            }
            return new ArrayList<>();
        }
    }

   static {
        //TRY sample 16,6avg
        dataTRY.add(new CurrencyRate(LocalDate.now().minusDays(6), CurrencyTypes.TRY, 5d));
        dataTRY.add(new CurrencyRate(LocalDate.now().minusDays(5), CurrencyTypes.TRY, 7d));
        dataTRY.add(new CurrencyRate(LocalDate.now().minusDays(4), CurrencyTypes.TRY, 8d));
        dataTRY.add(new CurrencyRate(LocalDate.now().minusDays(3), CurrencyTypes.TRY, 13d));
        dataTRY.add(new CurrencyRate(LocalDate.now().minusDays(2), CurrencyTypes.TRY, 55d));
        dataTRY.add(new CurrencyRate(LocalDate.now().minusDays(1), CurrencyTypes.TRY, 19.2));
        dataTRY.add(new CurrencyRate(LocalDate.now(), CurrencyTypes.TRY, 9d));

        //USD sample 8avg
        dataUSD.add(new CurrencyRate(LocalDate.now().minusDays(6), CurrencyTypes.USD, 5d));
        dataUSD.add(new CurrencyRate(LocalDate.now().minusDays(5), CurrencyTypes.USD, 6d));
        dataUSD.add(new CurrencyRate(LocalDate.now().minusDays(4), CurrencyTypes.USD, 7d));
        dataUSD.add(new CurrencyRate(LocalDate.now().minusDays(3), CurrencyTypes.USD, 8d));
        dataUSD.add(new CurrencyRate(LocalDate.now().minusDays(2), CurrencyTypes.USD, 9d));
        dataUSD.add(new CurrencyRate(LocalDate.now().minusDays(1), CurrencyTypes.USD, 10d));
        dataUSD.add(new CurrencyRate(LocalDate.now(), CurrencyTypes.USD, 11d));

        //EUR sample 4avg
        dataEUR.add(new CurrencyRate(LocalDate.now().minusDays(6), CurrencyTypes.EUR, 1d));
        dataEUR.add(new CurrencyRate(LocalDate.now().minusDays(5), CurrencyTypes.EUR, 2d));
        dataEUR.add(new CurrencyRate(LocalDate.now().minusDays(4), CurrencyTypes.EUR, 3d));
        dataEUR.add(new CurrencyRate(LocalDate.now().minusDays(3), CurrencyTypes.EUR, 4d));
        dataEUR.add(new CurrencyRate(LocalDate.now().minusDays(2), CurrencyTypes.EUR, 5d));
        dataEUR.add(new CurrencyRate(LocalDate.now().minusDays(1), CurrencyTypes.EUR, 6d));
        dataEUR.add(new CurrencyRate(LocalDate.now(), CurrencyTypes.EUR, 7d));

    }

    private static String getFormattedDateString(LocalDate date) {
        String ruShortName = date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.forLanguageTag("ru"));
        return ruShortName + " " + date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    @Test
    public void whenInputIsTomorrowUSDThenResultHas1String() {
        List<String> rate_usd_tomorrow = UserInputController.proceed("rate USD tomorrow", new StubParser(), new ArithmeticMean());

        assertEquals(1, rate_usd_tomorrow.size());
        assertEquals(TOMORROW + " - 8,00", rate_usd_tomorrow.get(0));
    }

    @Test
    public void whenInputIsTomorrowEURThenResultHas1String() {
        List<String> rate_eur_tomorrow = UserInputController.proceed("rate EUR tomorrow", new StubParser(), new ArithmeticMean());

        assertEquals(1, rate_eur_tomorrow.size());
        assertEquals(TOMORROW + " - 4,00", rate_eur_tomorrow.get(0));
    }

    @Test
    public void whenInputIsTomorrowTRYThenResultHas1String() {
        List<String> rate_try_tomorrow = UserInputController.proceed("rate TRY tomorrow", new StubParser(), new ArithmeticMean());

        assertEquals(1, rate_try_tomorrow.size());
        assertEquals(TOMORROW + " - 16,60", rate_try_tomorrow.get(0));
    }

    @Test
    public void whenInputIsWeekUSDThenResultHas7String() {
        List<String> rate_usd_tomorrow = UserInputController.proceed("rate USD week", new StubParser(), new ArithmeticMean());

        assertEquals(7, rate_usd_tomorrow.size());
        assertEquals(TOMORROW + " - 8,00", rate_usd_tomorrow.get(0));
        assertEquals(THE_DAY_AFTER_TOMORROW + " - 8,43", rate_usd_tomorrow.get(1));
        assertEquals(THIRD_DAY + " - 8,78", rate_usd_tomorrow.get(2));
        assertEquals(FOURTH_DAY + " - 9,03", rate_usd_tomorrow.get(3));
        assertEquals(FIFTH_DAY + " - 9,18", rate_usd_tomorrow.get(4));
        assertEquals(SIXTH_DAY + " - 9,20", rate_usd_tomorrow.get(5));
        assertEquals(SEVENTH_DAY + " - 9,09", rate_usd_tomorrow.get(6));
    }

    @Test
    public void whenInputIsWeekEURThenResultHas7String() {
        List<String> rate_eur_tomorrow = UserInputController.proceed("rate EUR week", new StubParser(), new ArithmeticMean());

        assertEquals(7, rate_eur_tomorrow.size());
        assertEquals(TOMORROW + " - 4,00", rate_eur_tomorrow.get(0));
        assertEquals(THE_DAY_AFTER_TOMORROW + " - 4,43", rate_eur_tomorrow.get(1));
        assertEquals(THIRD_DAY + " - 4,78", rate_eur_tomorrow.get(2));
        assertEquals(FOURTH_DAY + " - 5,03", rate_eur_tomorrow.get(3));
        assertEquals(FIFTH_DAY + " - 5,18", rate_eur_tomorrow.get(4));
        assertEquals(SIXTH_DAY + " - 5,20", rate_eur_tomorrow.get(5));
        assertEquals(SEVENTH_DAY + " - 5,09", rate_eur_tomorrow.get(6));
    }

    @Test
    public void whenInputIsWeekTRYThenResultHas7String() {
        List<String> rate_try_tomorrow = UserInputController.proceed("rate TRY week", new StubParser(), new ArithmeticMean());

        assertEquals(7, rate_try_tomorrow.size());
        assertEquals(TOMORROW + " - 16,60", rate_try_tomorrow.get(0));
        assertEquals(THE_DAY_AFTER_TOMORROW + " - 18,26", rate_try_tomorrow.get(1));
        assertEquals(THIRD_DAY + " - 19,87", rate_try_tomorrow.get(2));
        assertEquals(FOURTH_DAY + " - 21,56", rate_try_tomorrow.get(3));
        assertEquals(FIFTH_DAY + " - 22,78", rate_try_tomorrow.get(4));
        assertEquals(SIXTH_DAY + " - 18,18", rate_try_tomorrow.get(5));
        assertEquals(SEVENTH_DAY + " - 18,04", rate_try_tomorrow.get(6));
    }




}