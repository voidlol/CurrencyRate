package ru.liga.input;

import org.junit.jupiter.api.Test;
import ru.liga.currencies.CurrencyTypes;
import ru.liga.exceptions.*;
import ru.liga.prediction.AlgorithmTypes;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserCommandParserTest {


    @Test
    public void whenInputIsRateUSDDateTomorrowAlgLinearThenValid() {
        UserCommand expected = new UserCommand("rate USD -date tomorrow -alg linear");
        expected.setTargetDate(LocalDate.now().plusDays(1));
        expected.setAlgorithm(AlgorithmTypes.LINEAR.getAlgorithm());
        expected.setRange(false);
        expected.setCurrencyTypes(Collections.singleton(CurrencyTypes.USD));
        expected.setGraph(false);

        UserCommandParser userCommandParser = new UserCommandParser("rate USD -date tomorrow -alg linear");
        UserCommand actual = userCommandParser.getUserCommand();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenInputIsRateUSDEURPeriodMonthAlgLinearOutputGraphThenValid() {
        UserCommand expected = new UserCommand("rate USD,EUR -period month -alg linear -output graph");
        expected.setTargetDate(LocalDate.now().plusDays(30));
        expected.setAlgorithm(AlgorithmTypes.LINEAR.getAlgorithm());
        expected.setRange(true);
        Set<CurrencyTypes> currencies = new HashSet<>();
        currencies.add(CurrencyTypes.EUR);
        currencies.add(CurrencyTypes.USD);
        expected.setCurrencyTypes(currencies);
        expected.setGraph(true);

        UserCommandParser userCommandParser = new UserCommandParser("rate USD,EUR -period month -alg linear -output graph");
        UserCommand actual = userCommandParser.getUserCommand();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenInputIsRateUSDDateAlgLinearThenValid() {
        UserCommand expected = new UserCommand("rate USD,EUR -period month -alg linear -output graph");
        expected.setTargetDate(LocalDate.of(2023, 3, 28));
        expected.setAlgorithm(AlgorithmTypes.LINEAR.getAlgorithm());
        expected.setRange(false);
        Set<CurrencyTypes> currencies = new HashSet<>();
        currencies.add(CurrencyTypes.USD);
        expected.setCurrencyTypes(currencies);
        expected.setGraph(false);

        UserCommandParser userCommandParser = new UserCommandParser("rate USD -date 28.03.2023 -alg linear");
        UserCommand actual = userCommandParser.getUserCommand();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenInputIsNotRateThenInvalid() {
        assertThatThrownBy(() -> new UserCommandParser("asd USD -date tomorrow -alg linear"))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessageContaining(ErrorMessages.INVALID_COMMAND.getText());
    }

    @Test
    public void whenInputInvalidFormatThenError() {
        assertThatThrownBy(() -> new UserCommandParser("rate USD -date tomorrow dssad -alg linear"))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessageContaining(ErrorMessages.INVALID_INPUT_FORMAT.getText());
    }

    @Test
    public void whenInputInvalidDateThenError() {
        assertThatThrownBy(() -> new UserCommandParser("rate USD -date 25032022 -alg linear"))
                .isInstanceOf(InvalidRangeException.class)
                .hasMessageContaining(ErrorMessages.INVALID_DATE.getText());
    }

    @Test
    public void whenInputInvalidRangeThenError() {
        assertThatThrownBy(() -> new UserCommandParser("rate USD -period year -alg linear"))
                .isInstanceOf(InvalidRangeException.class)
                .hasMessageContaining(ErrorMessages.INVALID_PERIOD.getText());
    }

    @Test
    public void whenInputBothDateAndPeriodThenError() {
        assertThatThrownBy(() -> new UserCommandParser("rate USD -date tomorrow -period month -alg linear"))
                .isInstanceOf(InvalidRangeException.class)
                .hasMessageContaining(ErrorMessages.INVALID_DATE_AND_PERIOD.getText());
    }

    @Test
    public void whenInputNoDateOrPeriodThenError() {
        assertThatThrownBy(() -> new UserCommandParser("rate USD,EUR -alg linear -output graph"))
                .isInstanceOf(InvalidRangeException.class)
                .hasMessageContaining(ErrorMessages.INVALID_NO_DATE_OR_PERIOD.getText());
    }

    @Test
    public void whenInputNoAlgThenError() {
        assertThatThrownBy(() -> new UserCommandParser("rate USD,EUR -date tomorrow -output graph"))
                .isInstanceOf(InvalidAlgorithmException.class)
                .hasMessageContaining(ErrorMessages.INVALID_NO_ALG.getText());
    }

    @Test
    public void whenInputWrongAlgThenError() {
        assertThatThrownBy(() -> new UserCommandParser("rate USD -date tomorrow -alg wrong"))
                .isInstanceOf(InvalidAlgorithmException.class)
                .hasMessageContaining(ErrorMessages.INVALID_ALGORITHM.getText());
    }

    @Test
    public void whenInputMultipleCurrenciesAndNoPeriodThenError() {
        assertThatThrownBy(() -> new UserCommandParser("rate USD,EUR -date tomorrow -alg linear"))
                .isInstanceOf(InvalidRangeException.class)
                .hasMessageContaining(ErrorMessages.INVALID_NO_PERIOD_MULTI_CURRENCIES.getText());
    }

    @Test
    public void whenInputMultipleCurrenciesAndPeriodAndNoOutputThenError() {
        assertThatThrownBy(() -> new UserCommandParser("rate USD,EUR -period month -alg linear"))
                .isInstanceOf(InvalidOutputException.class)
                .hasMessageContaining(ErrorMessages.INVALID_OUTPUT.getText());
    }

    @Test
    public void whenInputMultipleCurrenciesAndPeriodAndWrongOutputThenError() {
        assertThatThrownBy(() -> new UserCommandParser("rate USD,EUR -period month -alg linear -output wrong"))
                .isInstanceOf(InvalidOutputException.class)
                .hasMessageContaining(ErrorMessages.INVALID_OUTPUT.getText());
    }

    @Test
    public void whenInputMoreThan5CurrenciesThenError() {
        assertThatThrownBy(() -> new UserCommandParser("rate USD,EUR,TRY,AMD,BGN,RUB -period month -alg linear -output graph"))
                .isInstanceOf(InvalidCurrencyException.class)
                .hasMessageContaining(ErrorMessages.INVALID_CURRENCY_AMOUNT.getText());
    }

    @Test
    public void whenInputSameCurrencyMoreThan1TimeThenError() {
        assertThatThrownBy(() -> new UserCommandParser("rate USD,EUR,USD -period month -alg linear -output graph"))
                .isInstanceOf(InvalidCurrencyException.class)
                .hasMessageContaining(ErrorMessages.INVALID_SAME_CURRENCY.getText());
    }

}