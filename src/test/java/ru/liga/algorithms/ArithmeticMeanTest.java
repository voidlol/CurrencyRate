package ru.liga.algorithms;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.liga.currencies.CurrencyRate;
import ru.liga.currencies.CurrencyTypes;
import ru.liga.executors.CommandExecutor;
import ru.liga.executors.DataCommandExecutor;
import ru.liga.executors.ExecutorController;
import ru.liga.input.UserCommand;
import ru.liga.input.UserCommandParser;
import ru.liga.output.CommandResult;
import ru.liga.repository.CurrencyRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ArithmeticMeanTest {

    private static final List<CurrencyRate> dataEUR = new ArrayList<>();
    private static final List<CurrencyRate> dataUSD = new ArrayList<>();
    private static final List<CurrencyRate> dataTRY = new ArrayList<>();

    private static final LocalDate TOMORROW = LocalDate.now().plusDays(1);
    private static final LocalDate THE_DAY_AFTER_TOMORROW = LocalDate.now().plusDays(2);
    private static final LocalDate THIRD_DAY = LocalDate.now().plusDays(3);
    private static final LocalDate FOURTH_DAY = LocalDate.now().plusDays(4);
    private static final LocalDate FIFTH_DAY = LocalDate.now().plusDays(5);
    private static final LocalDate SIXTH_DAY = LocalDate.now().plusDays(6);
    private static final LocalDate SEVENTH_DAY = LocalDate.now().plusDays(7);
    private static final CurrencyRepository currencyRepository = mock(CurrencyRepository.class);

   static {
        //TRY sample 16,6avg
        dataTRY.add(new CurrencyRate(LocalDate.now(), CurrencyTypes.TRY, 9d));
        dataTRY.add(new CurrencyRate(LocalDate.now().minusDays(1), CurrencyTypes.TRY, 19.2));
        dataTRY.add(new CurrencyRate(LocalDate.now().minusDays(2), CurrencyTypes.TRY, 55d));
        dataTRY.add(new CurrencyRate(LocalDate.now().minusDays(3), CurrencyTypes.TRY, 13d));
        dataTRY.add(new CurrencyRate(LocalDate.now().minusDays(4), CurrencyTypes.TRY, 8d));
        dataTRY.add(new CurrencyRate(LocalDate.now().minusDays(5), CurrencyTypes.TRY, 7d));
        dataTRY.add(new CurrencyRate(LocalDate.now().minusDays(6), CurrencyTypes.TRY, 5d));

        //USD sample 8avg
        dataUSD.add(new CurrencyRate(LocalDate.now(), CurrencyTypes.USD, 11d));
        dataUSD.add(new CurrencyRate(LocalDate.now().minusDays(1), CurrencyTypes.USD, 10d));
        dataUSD.add(new CurrencyRate(LocalDate.now().minusDays(2), CurrencyTypes.USD, 9d));
        dataUSD.add(new CurrencyRate(LocalDate.now().minusDays(3), CurrencyTypes.USD, 8d));
        dataUSD.add(new CurrencyRate(LocalDate.now().minusDays(4), CurrencyTypes.USD, 7d));
        dataUSD.add(new CurrencyRate(LocalDate.now().minusDays(5), CurrencyTypes.USD, 6d));
        dataUSD.add(new CurrencyRate(LocalDate.now().minusDays(6), CurrencyTypes.USD, 5d));

        //EUR sample 4avg
        dataEUR.add(new CurrencyRate(LocalDate.now(), CurrencyTypes.EUR, 7d));
        dataEUR.add(new CurrencyRate(LocalDate.now().minusDays(1), CurrencyTypes.EUR, 6d));
        dataEUR.add(new CurrencyRate(LocalDate.now().minusDays(2), CurrencyTypes.EUR, 5d));
        dataEUR.add(new CurrencyRate(LocalDate.now().minusDays(3), CurrencyTypes.EUR, 4d));
        dataEUR.add(new CurrencyRate(LocalDate.now().minusDays(4), CurrencyTypes.EUR, 3d));
        dataEUR.add(new CurrencyRate(LocalDate.now().minusDays(5), CurrencyTypes.EUR, 2d));
        dataEUR.add(new CurrencyRate(LocalDate.now().minusDays(6), CurrencyTypes.EUR, 1d));

    }

    @BeforeAll
    static void setMockito() {
        when(currencyRepository.getRates(CurrencyTypes.USD, 7)).thenReturn(dataUSD);
        when(currencyRepository.getRates(CurrencyTypes.EUR, 7)).thenReturn(dataEUR);
        when(currencyRepository.getRates(CurrencyTypes.TRY, 7)).thenReturn(dataTRY);
    }

    @Test
    public void whenInputIsTomorrowUSDThenResultHas1String() {
        UserCommand userCommand = new UserCommandParser( "rate USD -date tomorrow -alg mystic").getUserCommand();
        CommandExecutor executor = new ExecutorController(currencyRepository).getExecutor(userCommand);
        CommandResult result = executor.execute();

        List<CurrencyRate> rate_usd_tomorrow = result.getListResult();
        CurrencyRate tomorrowRate = new CurrencyRate(TOMORROW, CurrencyTypes.USD, 8d);

        assertThat(rate_usd_tomorrow.size()).isEqualTo(1);
        assertThat(rate_usd_tomorrow.get(0)).isEqualTo(tomorrowRate);
    }

    @Test
    public void whenInputIsTomorrowEURThenResultHas1String() {
        UserCommand userCommand = new UserCommandParser("rate EUR -date tomorrow -alg mystic").getUserCommand();
        CommandExecutor executor = new ExecutorController(currencyRepository).getExecutor(userCommand);
        CommandResult result = executor.execute();

        List<CurrencyRate> rate_eur_tomorrow = result.getListResult();
        CurrencyRate tomorrowRate = new CurrencyRate(TOMORROW, CurrencyTypes.EUR, 4d);

        assertThat(rate_eur_tomorrow.size()).isEqualTo(1);
        assertThat(rate_eur_tomorrow.get(0)).isEqualTo(tomorrowRate);
    }

    @Test
    public void whenInputIsTomorrowTRYThenResultHas1String() {
        UserCommand userCommand = new UserCommandParser("rate TRY -date tomorrow -alg mystic").getUserCommand();
        CommandExecutor executor = new ExecutorController(currencyRepository).getExecutor(userCommand);
        CommandResult result = executor.execute();

        List<CurrencyRate> rate_try_tomorrow = result.getListResult();
        CurrencyRate tomorrowRate = new CurrencyRate(TOMORROW, CurrencyTypes.TRY, 16.6d);

        assertThat(rate_try_tomorrow.size()).isEqualTo(1);
        assertThat(rate_try_tomorrow.get(0)).isEqualTo(tomorrowRate);
    }

    @Test
    public void whenInputIsWeekUSDThenResultHas7String() {
        UserCommand userCommand = new UserCommandParser("rate USD -period week -alg mystic").getUserCommand();
        CommandExecutor executor = new ExecutorController(currencyRepository).getExecutor(userCommand);
        CommandResult result = executor.execute();
        List<CurrencyRate> rate_usd_week = result.getListResult();
        CurrencyRate tomorrowRate = new CurrencyRate(TOMORROW, CurrencyTypes.USD, 8d);
        CurrencyRate secondDayRate = new CurrencyRate(THE_DAY_AFTER_TOMORROW, CurrencyTypes.USD, 8.43);
        CurrencyRate thirdDayRate = new CurrencyRate(THIRD_DAY, CurrencyTypes.USD, 8.78);
        CurrencyRate fourthDayRate = new CurrencyRate(FOURTH_DAY, CurrencyTypes.USD, 9.03d);
        CurrencyRate fifthDayRate = new CurrencyRate(FIFTH_DAY, CurrencyTypes.USD, 9.18);
        CurrencyRate sixthDayRate = new CurrencyRate(SIXTH_DAY, CurrencyTypes.USD, 9.2);
        CurrencyRate seventhDayRate = new CurrencyRate(SEVENTH_DAY, CurrencyTypes.USD, 9.09);

        assertThat(rate_usd_week.size()).isEqualTo(7);
        assertThat(rate_usd_week.get(0)).isEqualTo(tomorrowRate);
        assertThat(rate_usd_week.get(1)).isEqualTo(secondDayRate);
        assertThat(rate_usd_week.get(2)).isEqualTo(thirdDayRate);
        assertThat(rate_usd_week.get(3)).isEqualTo(fourthDayRate);
        assertThat(rate_usd_week.get(4)).isEqualTo(fifthDayRate);
        assertThat(rate_usd_week.get(5)).isEqualTo(sixthDayRate);
        assertThat(rate_usd_week.get(6)).isEqualTo(seventhDayRate);
    }

    @Test
    public void whenInputIsWeekEURThenResultHas7String() {
        UserCommand userCommand = new UserCommandParser("rate EUR -period week -alg mystic").getUserCommand();
        CommandExecutor executor = new ExecutorController(currencyRepository).getExecutor(userCommand);
        CommandResult result = executor.execute();
        List<CurrencyRate> rate_eur_week = result.getListResult();
        CurrencyRate tomorrowRate = new CurrencyRate(TOMORROW, CurrencyTypes.EUR, 4d);
        CurrencyRate secondDayRate = new CurrencyRate(THE_DAY_AFTER_TOMORROW, CurrencyTypes.EUR, 4.43);
        CurrencyRate thirdDayRate = new CurrencyRate(THIRD_DAY, CurrencyTypes.EUR, 4.78);
        CurrencyRate fourthDayRate = new CurrencyRate(FOURTH_DAY, CurrencyTypes.EUR, 5.03);
        CurrencyRate fifthDayRate = new CurrencyRate(FIFTH_DAY, CurrencyTypes.EUR, 5.18);
        CurrencyRate sixthDayRate = new CurrencyRate(SIXTH_DAY, CurrencyTypes.EUR, 5.2);
        CurrencyRate seventhDayRate = new CurrencyRate(SEVENTH_DAY, CurrencyTypes.EUR, 5.09);

        assertThat(rate_eur_week.size()).isEqualTo(7);
        assertThat(rate_eur_week.get(0)).isEqualTo(tomorrowRate);
        assertThat(rate_eur_week.get(1)).isEqualTo(secondDayRate);
        assertThat(rate_eur_week.get(2)).isEqualTo(thirdDayRate);
        assertThat(rate_eur_week.get(3)).isEqualTo(fourthDayRate);
        assertThat(rate_eur_week.get(4)).isEqualTo(fifthDayRate);
        assertThat(rate_eur_week.get(5)).isEqualTo(sixthDayRate);
        assertThat(rate_eur_week.get(6)).isEqualTo(seventhDayRate);
    }

    @Test
    public void whenInputIsWeekTRYThenResultHas7String() {
        UserCommand userCommand = new UserCommandParser("rate TRY -period week -alg mystic").getUserCommand();
        CommandExecutor executor = new ExecutorController(currencyRepository).getExecutor(userCommand);
        CommandResult result = executor.execute();
        List<CurrencyRate> rate_try_week = result.getListResult();
        CurrencyRate tomorrowRate = new CurrencyRate(TOMORROW, CurrencyTypes.TRY, 16.6d);
        CurrencyRate secondDayRate = new CurrencyRate(THE_DAY_AFTER_TOMORROW, CurrencyTypes.TRY, 18.26);
        CurrencyRate thirdDayRate = new CurrencyRate(THIRD_DAY, CurrencyTypes.TRY, 19.87);
        CurrencyRate fourthDayRate = new CurrencyRate(FOURTH_DAY, CurrencyTypes.TRY, 21.56);
        CurrencyRate fifthDayRate = new CurrencyRate(FIFTH_DAY, CurrencyTypes.TRY, 22.78);
        CurrencyRate sixthDayRate = new CurrencyRate(SIXTH_DAY, CurrencyTypes.TRY, 18.18);
        CurrencyRate seventhDayRate = new CurrencyRate(SEVENTH_DAY, CurrencyTypes.TRY, 18.04);

        assertThat(rate_try_week.size()).isEqualTo(7);
        assertThat(rate_try_week.get(0)).isEqualTo(tomorrowRate);
        assertThat(rate_try_week.get(1)).isEqualTo(secondDayRate);
        assertThat(rate_try_week.get(2)).isEqualTo(thirdDayRate);
        assertThat(rate_try_week.get(3)).isEqualTo(fourthDayRate);
        assertThat(rate_try_week.get(4)).isEqualTo(fifthDayRate);
        assertThat(rate_try_week.get(5)).isEqualTo(sixthDayRate);
        assertThat(rate_try_week.get(6)).isEqualTo(seventhDayRate);
    }




}