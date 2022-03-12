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
        UserCommand actual = UserCommand.getBuilder(input).build();


        assertThat(LocalDate.now().plusDays(1)).isEqualTo(actual.getTargetDate());
        assertThat(AlgorithmTypes.LINEAR.getAlgorithm()).isEqualTo(actual.getAlgorithm());
        assertThat(actual.getRangeType()).isNull();
        assertThat(actual.isGraph()).isFalse();
        assertThat(Collections.singletonList(CurrencyTypes.USD)).isEqualTo(actual.getCurrencyTypes());
    }


    @Test
    void whenInputIsNotRateThenInvalid() {
        UserCommand.UserCommandBuilder builder = UserCommand.getBuilder("asd USD -date tomorrow -alg linear");
        assertThatThrownBy(builder::build)
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessageContaining(ErrorMessages.INVALID_COMMAND.getText());
    }

    @Test
    void whenInputInvalidFormatThenError() {
        UserCommand.UserCommandBuilder builder = UserCommand.getBuilder("rate USD -date tomorrow dssad -alg linear");
        assertThatThrownBy(builder::build)
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessageContaining(ErrorMessages.INVALID_INPUT_FORMAT.getText());
    }

    @Test
    void whenInputInvalidDateThenError() {
        UserCommand.UserCommandBuilder builder = UserCommand.getBuilder("rate USD -date 25032022 -alg linear");
        assertThatThrownBy(builder::build)
                .isInstanceOf(InvalidRangeException.class)
                .hasMessageContaining(ErrorMessages.INVALID_DATE.getText());
    }

    @Test
    void whenInputInvalidRangeThenError() {
        UserCommand.UserCommandBuilder builder = UserCommand.getBuilder("rate USD -period year -alg linear");
        assertThatThrownBy(builder::build)
                .isInstanceOf(InvalidRangeException.class)
                .hasMessageContaining(ErrorMessages.INVALID_PERIOD.getText());
    }

    @Test
    void whenInputBothDateAndPeriodThenError() {
        UserCommand.UserCommandBuilder builder = UserCommand.getBuilder("rate USD -date tomorrow -period month -alg linear");
        assertThatThrownBy(builder::build)
                .isInstanceOf(InvalidRangeException.class)
                .hasMessageContaining(ErrorMessages.INVALID_DATE_AND_PERIOD.getText());
    }

    @Test
    void whenInputNoDateOrPeriodThenError() {
        UserCommand.UserCommandBuilder builder = UserCommand.getBuilder("rate USD,EUR -alg linear -output graph");
        assertThatThrownBy(builder::build)
                .isInstanceOf(InvalidRangeException.class)
                .hasMessageContaining(ErrorMessages.INVALID_NO_DATE_OR_PERIOD.getText());
    }

    @Test
    void whenInputNoAlgThenError() {
        UserCommand.UserCommandBuilder builder = UserCommand.getBuilder("rate USD,EUR -date tomorrow -output graph");
        assertThatThrownBy(builder::build)
                .isInstanceOf(InvalidAlgorithmException.class)
                .hasMessageContaining(ErrorMessages.INVALID_NO_ALG.getText());
    }

    @Test
    void whenInputWrongAlgThenError() {
        UserCommand.UserCommandBuilder builder = UserCommand.getBuilder("rate USD -date tomorrow -alg wrong");
        assertThatThrownBy(builder::build)
                .isInstanceOf(InvalidAlgorithmException.class)
                .hasMessageContaining(ErrorMessages.INVALID_ALGORITHM.getText());
    }

    @Test
    void whenInputMultipleCurrenciesAndNoOutputThenError() {
        UserCommand.UserCommandBuilder builder = UserCommand.getBuilder("rate USD,EUR -date tomorrow -alg linear");
        assertThatThrownBy(builder::build)
                .isInstanceOf(InvalidCurrencyException.class)
                .hasMessageContaining(ErrorMessages.INVALID_OUTPUT.getText());
    }

    @Test
    void whenInputMultipleCurrenciesAndPeriodAndNoOutputThenError() {
        UserCommand.UserCommandBuilder builder = UserCommand.getBuilder("rate USD,EUR -period month -alg linear");
        assertThatThrownBy(builder::build)
                .isInstanceOf(InvalidCurrencyException.class)
                .hasMessageContaining(ErrorMessages.INVALID_OUTPUT.getText());
    }

    @Test
    void whenInputMultipleCurrenciesAndPeriodAndWrongOutputThenError() {
        UserCommand.UserCommandBuilder builder = UserCommand.getBuilder("rate USD,EUR -period month -alg linear -output wrong");
        assertThatThrownBy(builder::build)
                .isInstanceOf(InvalidCurrencyException.class)
                .hasMessageContaining(ErrorMessages.INVALID_OUTPUT.getText());
    }

    @Test
    void whenInputMoreThan5CurrenciesThenError() {
        UserCommand.UserCommandBuilder builder = UserCommand.getBuilder("rate USD,EUR,TRY,AMD,BGN,RUB -period month -alg linear -output graph");
        assertThatThrownBy(builder::build)
                .isInstanceOf(InvalidCurrencyException.class)
                .hasMessageContaining(ErrorMessages.INVALID_CURRENCY_AMOUNT.getText());
    }

    @Test
    void whenInputSameCurrencyMoreThan1TimeThenError() {
        UserCommand.UserCommandBuilder builder = UserCommand.getBuilder("rate USD,EUR,USD -period month -alg linear -output graph");
        assertThatThrownBy(builder::build)
                .isInstanceOf(InvalidCurrencyException.class)
                .hasMessageContaining(ErrorMessages.INVALID_SAME_CURRENCY.getText());
    }
}