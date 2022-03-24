package ru.liga.input;

import org.junit.jupiter.api.Test;
import ru.liga.exception.*;
import ru.liga.type.AlgorithmTypes;
import ru.liga.type.CurrencyTypes;
import ru.liga.type.ErrorMessages;
import ru.liga.type.OutputType;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UserCommandBuilderTest {

    @Test
    void whenInputValid() {
        String input = "rate USD -date tomorrow -alg linear";
        UserCommand actual = UserCommand.createFromString(input);
        Period expectedPeriod = new Period(LocalDate.now().plusDays(1), false);

        assertThat(actual.getPeriod()).isEqualTo(expectedPeriod);
        assertThat(actual.getAlgorithm()).isEqualTo(AlgorithmTypes.LINEAR.getAlgorithm());
        assertThat(actual.getOutputType()).isNull();
        assertThat(actual.getCurrencyTypes()).isEqualTo(Collections.singletonList(CurrencyTypes.USD));
    }

    @Test
    void whenInputHasOutputListThenValidCommand() {
        String input = "rate USD -period month -alg linear -output list";
        UserCommand actual = UserCommand.createFromString(input);
        Period expectedPeriod = new Period(LocalDate.now().plusDays(30), true);

        assertThat(actual.getPeriod()).isEqualTo(expectedPeriod);
        assertThat(actual.getAlgorithm()).isEqualTo(AlgorithmTypes.LINEAR.getAlgorithm());
        assertThat(actual.getOutputType()).isEqualTo(OutputType.LIST);
        assertThat(actual.getCurrencyTypes()).isEqualTo(Collections.singletonList(CurrencyTypes.USD));
    }

    @Test
    void whenInputIsNotRateThenInvalidArgumentException() {
        assertThatThrownBy(() -> UserCommand.createFromString("asd USD -date tomorrow -alg linear"))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessageContaining(ErrorMessages.INVALID_COMMAND.getText());
    }

    @Test
    void whenExtraCommasThenValidCommand() {
        String input = "rate USD,,EUR, -period month -alg linear -output graph";
        UserCommand actual = UserCommand.createFromString(input);
        Period expectedPeriod = new Period(LocalDate.now().plusDays(30), true);
        List<CurrencyTypes> expectedCurrencies = Arrays.asList(CurrencyTypes.USD, CurrencyTypes.EUR);

        assertThat(actual.getPeriod()).isEqualTo(expectedPeriod);
        assertThat(actual.getAlgorithm()).isEqualTo(AlgorithmTypes.LINEAR.getAlgorithm());
        assertThat(actual.getOutputType()).isEqualTo(OutputType.GRAPH);
        assertThat(actual.getCurrencyTypes()).isEqualTo(expectedCurrencies);
    }


    @Test
    void whenInputPeriodAndNoOutputThenInvalidRangeException() {
        assertThatThrownBy(() -> UserCommand.createFromString("rate USD -period week -alg linear"))
                .isInstanceOf(InvalidRangeException.class)
                .hasMessageContaining(ErrorMessages.INVALID_NO_OUTPUT.getText());
    }

    @Test
    void whenInputInvalidFormatThenInvalidArgumentException() {
        assertThatThrownBy(() -> UserCommand.createFromString("rate USD -date tomorrow dssad -alg linear"))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessageContaining(ErrorMessages.INVALID_INPUT_FORMAT.getText());
    }

    @Test
    void whenInputInvalidCurrencyThenInvalidCurrencyException() {
        assertThatThrownBy(() -> UserCommand.createFromString("rate RUB -period month -alg linear -output graph"))
                .isInstanceOf(InvalidCurrencyException.class)
                .hasMessageContaining(ErrorMessages.INVALID_CURRENCY.getText());
    }

    @Test
    void whenInputInvalidDateThenInvalidRangeException() {
        assertThatThrownBy(() -> UserCommand.createFromString("rate USD -date 25032022 -alg linear"))
                .isInstanceOf(InvalidRangeException.class)
                .hasMessageContaining(ErrorMessages.INVALID_DATE.getText());
    }

    @Test
    void whenInputInvalidRangeThenInvalidRangeException() {
        assertThatThrownBy(() -> UserCommand.createFromString("rate USD -period year -alg linear -output list"))
                .isInstanceOf(InvalidRangeException.class)
                .hasMessageContaining(ErrorMessages.INVALID_PERIOD.getText());
    }

    @Test
    void whenInputBothDateAndPeriodThenInvalidRangeException() {
        assertThatThrownBy(() -> UserCommand.createFromString("rate USD -date tomorrow -period month -alg linear"))
                .isInstanceOf(InvalidRangeException.class)
                .hasMessageContaining(ErrorMessages.INVALID_DATE_AND_PERIOD.getText());
    }

    @Test
    void whenInputNoDateOrPeriodThenInvalidRangeException() {
        assertThatThrownBy(() -> UserCommand.createFromString("rate USD,EUR -alg linear -output graph"))
                .isInstanceOf(InvalidRangeException.class)
                .hasMessageContaining(ErrorMessages.INVALID_NO_DATE_OR_PERIOD.getText());
    }

    @Test
    void whenInputNoAlgThenInvalidAlgorithmException() {
        assertThatThrownBy(() -> UserCommand.createFromString("rate USD,EUR -period week -output graph"))
                .isInstanceOf(InvalidAlgorithmException.class)
                .hasMessageContaining(ErrorMessages.INVALID_NO_ALG.getText());
    }

    @Test
    void whenInputWrongAlgThenInvalidAlgorithmException() {
        assertThatThrownBy(() -> UserCommand.createFromString("rate USD -date tomorrow -alg wrong"))
                .isInstanceOf(InvalidAlgorithmException.class)
                .hasMessageContaining(ErrorMessages.INVALID_ALGORITHM.getText());
    }

    @Test
    void whenInputMultipleCurrenciesAndPeriodAndNoOutputThenInvalidOutputException() {
        assertThatThrownBy(() -> UserCommand.createFromString("rate USD,EUR -period month -alg linear"))
                .isInstanceOf(InvalidOutputException.class)
                .hasMessageContaining(ErrorMessages.INVALID_NO_OUTPUT.getText());
    }

    @Test
    void whenInputMultipleCurrenciesAndPeriodAndWrongOutputThenInvalidOutputException() {
        assertThatThrownBy(() -> UserCommand.createFromString("rate USD,EUR -period month -alg linear -output wrong"))
                .isInstanceOf(InvalidOutputException.class)
                .hasMessageContaining(ErrorMessages.INVALID_NO_GRAPH_MULTI_CURRENCIES.getText());
    }

    @Test
    void whenInputMoreThan5CurrenciesThenInvalidCurrencyException() {
        assertThatThrownBy(() -> UserCommand.createFromString("rate USD,EUR,TRY,AMD,BGN,RUB -period month -alg linear -output graph"))
                .isInstanceOf(InvalidCurrencyException.class)
                .hasMessageContaining(ErrorMessages.INVALID_CURRENCY_AMOUNT.getText());
    }

    @Test
    void whenInputNoPeriodWhenOutputGraphThenInvalidRangeException() {
        assertThatThrownBy(() -> UserCommand.createFromString("rate EUR -date tomorrow -alg linear -output graph"))
                .isInstanceOf(InvalidRangeException.class)
                .hasMessageContaining(ErrorMessages.INVALID_NO_PERIOD_WHEN_OUTPUT.getText());
    }

    @Test
    void whenInputSameCurrencyMoreThan1TimeThenInvalidCurrencyException() {
        assertThatThrownBy(() -> UserCommand.createFromString("rate USD,EUR,USD -period month -alg linear -output graph"))
                .isInstanceOf(InvalidCurrencyException.class)
                .hasMessageContaining(ErrorMessages.INVALID_SAME_CURRENCY.getText());
    }
}