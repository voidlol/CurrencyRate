package ru.liga.algorithms;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.liga.currencies.CurrencyRate;
import ru.liga.currencies.CurrencyTypes;
import ru.liga.input.UserCommand;
import ru.liga.input.UserCommandParser;
import ru.liga.repository.CurrencyRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ActualTest {

    private static final CurrencyRepository repository = mock(CurrencyRepository.class);

    @BeforeAll
    static void setMockito() {
        List<CurrencyRate> USD = new ArrayList<>();
        for (int i = 0; i < 1100; i++) {
            USD.add(new CurrencyRate(LocalDate.now().minusDays(i), CurrencyTypes.USD, 10D));
        }
        when(repository.getRates(CurrencyTypes.USD, 1)).thenReturn(USD);
        when(repository.getRateForDate(eq(CurrencyTypes.USD), any(LocalDate.class))).thenAnswer(
                invocation -> USD.stream()
                        .filter(currencyRate -> currencyRate.getDate().isEqual(invocation.getArgumentAt(1, LocalDate.class)))
                        .findFirst()
                        .get());
    }

    @Test
    void whenUSDThenOneResult() {
        UserCommand userCommand = new UserCommandParser("rate USD -date 20.03.2022 -alg actual").getUserCommand();
        userCommand.setRepository(repository);
        userCommand.execute();

        List<CurrencyRate> forecast = userCommand.getForecast();
        CurrencyRate actual = forecast.get(0);
        CurrencyRate expected = new CurrencyRate(LocalDate.of(2022, 3, 20), CurrencyTypes.USD, 20D);
        assertThat(expected).isEqualTo(actual);
    }
}