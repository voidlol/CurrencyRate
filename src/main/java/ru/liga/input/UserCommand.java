package ru.liga.input;

import lombok.Getter;
import ru.liga.algorithm.CurrencyForecaster;
import ru.liga.type.CommandType;
import ru.liga.type.CurrencyTypes;
import ru.liga.type.OutputType;
import ru.liga.validator.CommandValidatorService;

import java.util.List;
import java.util.Map;

@Getter
public class UserCommand {

    private final String inputString;
    private final CommandType commandType;
    private final List<CurrencyTypes> currencyTypes;
    private final Period period;
    private final CurrencyForecaster algorithm;
    private final OutputType outputType;

    private UserCommand(String inputString, CommandType commandType,
                       List<CurrencyTypes> currencyTypes, Period period,
                       CurrencyForecaster algorithm, OutputType outputType) {
        this.inputString = inputString;
        this.commandType = commandType;
        this.currencyTypes = currencyTypes;
        this.period = period;
        this.algorithm = algorithm;
        this.outputType = outputType;
    }

    /**
     * Builds valid UserCommand object
     * If fails during validation:
     *
     * @return UserCommand
     * @throws ru.liga.exception.BaseException - with error message
     */
    public static UserCommand createFromString(String inputString) {
        Map<String, String> args = InputStringParser.parse(inputString);
        CommandValidatorService validator = new CommandValidatorService(args);
        CommandType commandType = validator.validateAndGetCommandType();
        List<CurrencyTypes> currencyTypes = validator.validateAndGetCurrency();
        Period period = validator.validateAndGetPeriod();
        CurrencyForecaster algorithm = validator.validateAndGetAlgorithm();
        OutputType outputType = validator.validateAndGetOutput();
        return new UserCommand(inputString, commandType, currencyTypes, period, algorithm, outputType);
    }
}
