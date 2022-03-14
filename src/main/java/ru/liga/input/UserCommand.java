package ru.liga.input;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import ru.liga.algorithm.CurrencyForecaster;
import ru.liga.type.CurrencyTypes;
import ru.liga.validator.*;

import java.util.List;
import java.util.Map;

@Setter(AccessLevel.PRIVATE)
@Getter
public class UserCommand {

    private Period period;
    private CurrencyForecaster algorithm;
    private List<CurrencyTypes> currencyTypes;
    private boolean isGraph;
    private String inputString;

    private UserCommand(String inputString) {
        this.inputString = inputString;
    }

    /**
     * Builds valid UserCommand object
     * If fails during validation:
     * @throws ru.liga.exception.BaseException - with error message
     * @return UserCommand
     */
    public static UserCommand createFromString(String inputString) {
        final Validator<Boolean> commandValidator = new CommandValidator();
        final Validator<List<CurrencyTypes>> currencyValidator = new CurrencyValidator();
        final Validator<Period> periodValidator = new DateValidator();
        final Validator<CurrencyForecaster> algorithmValidator = new AlgorithmValidator();
        final Validator<Boolean> outputValidator = new OutputValidator();
        UserCommand userCommand = new UserCommand(inputString);
        Map<String, String> args = InputStringParser.parse(inputString);
        commandValidator.validateAndGet(args);
        userCommand.setPeriod(periodValidator.validateAndGet(args));
        userCommand.setAlgorithm(algorithmValidator.validateAndGet(args));
        userCommand.setCurrencyTypes(currencyValidator.validateAndGet(args));
        userCommand.setGraph(outputValidator.validateAndGet(args));
        return userCommand;
    }
}
