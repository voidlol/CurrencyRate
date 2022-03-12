package ru.liga.algorithm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.liga.currency.CurrencyRate;
import ru.liga.type.CurrencyTypes;
import ru.liga.executor.CommandExecutor;
import ru.liga.executor.ExecutorController;
import ru.liga.input.UserCommand;
import ru.liga.output.CommandResult;
import ru.liga.repository.CurrencyRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LinearTest {

    private static final CurrencyRepository repository = mock(CurrencyRepository.class);

    @BeforeEach
     void setMockito() {
        List<CurrencyRate> USD = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            USD.add(new CurrencyRate(LocalDate.now().minusDays(i), CurrencyTypes.USD, 30D - i));
        }
        when(repository.getRates(CurrencyTypes.USD, 30)).thenReturn(USD);
    }

    @Test
    void whenInputUSDTomorrowThen1String() {
        UserCommand userCommand = UserCommand.getBuilder("rate USD -date tomorrow -alg linear").build();
        CommandExecutor executor = new ExecutorController(repository).getExecutor(userCommand);
        CommandResult result = executor.execute();

        List<CurrencyRate> forecast = result.getListResult();
        CurrencyRate expected = new CurrencyRate(LocalDate.now().plusDays(1), CurrencyTypes.USD, 31D);
        CurrencyRate actual = forecast.get(0);
        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void whenInputUSDDateThen1String() {
        UserCommand userCommand = UserCommand.getBuilder("rate USD -date 26.03.2022 -alg linear").build();
        CommandExecutor executor = new ExecutorController(repository).getExecutor(userCommand);
        CommandResult result = executor.execute();

        List<CurrencyRate> forecast = result.getListResult();
        CurrencyRate expected = new CurrencyRate(LocalDate.of(2022, 3, 26), CurrencyTypes.USD, 47D);
        CurrencyRate actual = forecast.get(0);
        assertThat(expected).isEqualTo(actual);
    }
}