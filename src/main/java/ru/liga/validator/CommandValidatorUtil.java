package ru.liga.validator;

import ru.liga.algorithm.CurrencyForecaster;
import ru.liga.input.Period;
import ru.liga.type.CurrencyTypes;

import java.util.List;
import java.util.Map;

public class CommandValidatorUtil {

    private static final Validator<Period> dateValidator = new DateValidator();
    private static final Validator<Boolean> commandValidator = new CommandValidator();
    private static final Validator<List<CurrencyTypes>> currencyValidator = new CurrencyValidator();
    private static final Validator<CurrencyForecaster> algValidator = new AlgorithmValidator();
    private static final Validator<Boolean> outputValidator = new OutputValidator();
    private final Map<String, String> args;

    public CommandValidatorUtil(Map<String, String> args) {
        this.args = args;
    }

    public Period validateAndGetDate() {
        return dateValidator.validateAndGet(args);
    }

    public Boolean validateAndGetCommand() {
        return commandValidator.validateAndGet(args);
    }

    public List<CurrencyTypes> validateAndGetCurrency() {
        return currencyValidator.validateAndGet(args);
    }

    public CurrencyForecaster validateAndGetAlgorithm() {
        return algValidator.validateAndGet(args);
    }

    public Boolean validateAndGetOutput() {
        return outputValidator.validateAndGet(args);
    }
}
