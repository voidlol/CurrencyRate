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
        UserCommand userCommand = new UserCommand(inputString);
        Map<String, String> args = InputStringParser.parse(inputString);
        CommandValidatorUtil validator = new CommandValidatorUtil(args);
        validator.validateAndGetCommand();
        userCommand.setPeriod(validator.validateAndGetDate());
        userCommand.setAlgorithm(validator.validateAndGetAlgorithm());
        userCommand.setCurrencyTypes(validator.validateAndGetCurrency());
        userCommand.setGraph(validator.validateAndGetOutput());
        return userCommand;
    }
}
