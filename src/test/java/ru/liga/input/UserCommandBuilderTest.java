package ru.liga.input;

import org.junit.jupiter.api.Test;
import ru.liga.exception.*;
import ru.liga.type.AlgorithmTypes;
import ru.liga.type.CurrencyTypes;
import ru.liga.type.ErrorMessages;

import java.time.LocalDate;
import java.util.Collections;

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
        assertThat(actual.isGraph()).isFalse();
        assertThat(actual.getCurrencyTypes()).isEqualTo(Collections.singletonList(CurrencyTypes.USD));
    }


    @Test
    void whenInputIsNotRateThenInvalid() {
        assertThatThrownBy(() -> UserCommand.createFromString("asd USD -date tomorrow -alg linear"))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessageContaining(ErrorMessages.INVALID_COMMAND.getText());
    }

    @Test
    void whenInputInvalidFormatThenError() {
        assertThatThrownBy(() -> UserCommand.createFromString("rate USD -date tomorrow dssad -alg linear"))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessageContaining(ErrorMessages.INVALID_INPUT_FORMAT.getText());
    }

    @Test
    void whenInputInvalidCurrencyThenError() {
        assertThatThrownBy(() -> UserCommand.createFromString("rate RUB -period month -alg linear -output graph"))
                .isInstanceOf(InvalidCurrencyException.class)
                .hasMessageContaining(ErrorMessages.INVALID_CURRENCY.getText());
    }

    @Test
    void whenInputInvalidDateThenError() {
        assertThatThrownBy(() -> UserCommand.createFromString("rate USD -date 25032022 -alg linear"))
                .isInstanceOf(InvalidRangeException.class)
                .hasMessageContaining(ErrorMessages.INVALID_DATE.getText());
    }

    @Test
    void whenInputInvalidRangeThenError() {
        assertThatThrownBy(() -> UserCommand.createFromString("rate USD -period year -alg linear"))
                .isInstanceOf(InvalidRangeException.class)
                .hasMessageContaining(ErrorMessages.INVALID_PERIOD.getText());
    }

    @Test
    void whenInputBothDateAndPeriodThenError() {
        assertThatThrownBy(() -> UserCommand.createFromString("rate USD -date tomorrow -period month -alg linear"))
                .isInstanceOf(InvalidRangeException.class)
                .hasMessageContaining(ErrorMessages.INVALID_DATE_AND_PERIOD.getText());
    }

    @Test
    void whenInputNoDateOrPeriodThenError() {
        assertThatThrownBy(() -> UserCommand.createFromString("rate USD,EUR -alg linear -output graph"))
                .isInstanceOf(InvalidRangeException.class)
                .hasMessageContaining(ErrorMessages.INVALID_NO_DATE_OR_PERIOD.getText());
    }

    @Test
    void whenInputNoAlgThenError() {
        assertThatThrownBy(() -> UserCommand.createFromString("rate USD,EUR -period week -output graph"))
                .isInstanceOf(InvalidAlgorithmException.class)
                .hasMessageContaining(ErrorMessages.INVALID_NO_ALG.getText());
    }

    @Test
    void whenInputWrongAlgThenError() {
        assertThatThrownBy(() -> UserCommand.createFromString("rate USD -date tomorrow -alg wrong"))
                .isInstanceOf(InvalidAlgorithmException.class)
                .hasMessageContaining(ErrorMessages.INVALID_ALGORITHM.getText());
    }

    @Test
    void whenInputMultipleCurrenciesAndNoOutputThenError() {
        assertThatThrownBy(() -> UserCommand.createFromString("rate USD,EUR -date tomorrow -alg linear"))
                .isInstanceOf(InvalidCurrencyException.class)
                .hasMessageContaining(ErrorMessages.INVALID_NO_OUTPUT.getText());
    }

    @Test
    void whenInputMultipleCurrenciesAndPeriodAndNoOutputThenError() {
        assertThatThrownBy(() -> UserCommand.createFromString("rate USD,EUR -period month -alg linear"))
                .isInstanceOf(InvalidCurrencyException.class)
                .hasMessageContaining(ErrorMessages.INVALID_NO_OUTPUT.getText());
    }

    @Test
    void whenInputMultipleCurrenciesAndPeriodAndWrongOutputThenError() {
        assertThatThrownBy(() -> UserCommand.createFromString("rate USD,EUR -period month -alg linear -output wrong"))
                .isInstanceOf(InvalidOutputException.class)
                .hasMessageContaining(ErrorMessages.INVALID_OUTPUT.getText());
    }

    @Test
    void whenInputMoreThan5CurrenciesThenError() {
        assertThatThrownBy(() -> UserCommand.createFromString("rate USD,EUR,TRY,AMD,BGN,RUB -period month -alg linear -output graph"))
                .isInstanceOf(InvalidCurrencyException.class)
                .hasMessageContaining(ErrorMessages.INVALID_CURRENCY_AMOUNT.getText());
    }

    @Test
    void whenInputNoPeriodWhenOutputGraphThenError() {
        assertThatThrownBy(() -> UserCommand.createFromString("rate EUR -date tomorrow -alg linear -output graph"))
                .isInstanceOf(InvalidRangeException.class)
                .hasMessageContaining(ErrorMessages.INVALID_NO_PERIOD_WHEN_OUTPUT_GRAPH.getText());
    }

    @Test
    void whenInputSameCurrencyMoreThan1TimeThenError() {
        assertThatThrownBy(() -> UserCommand.createFromString("rate USD,EUR,USD -period month -alg linear -output graph"))
                .isInstanceOf(InvalidCurrencyException.class)
                .hasMessageContaining(ErrorMessages.INVALID_SAME_CURRENCY.getText());
    }
}