package ru.liga.validator;

import ru.liga.algorithm.CurrencyForecaster;
import ru.liga.input.Period;
import ru.liga.type.CommandType;
import ru.liga.type.CurrencyTypes;

import java.util.List;
import java.util.Map;

public class CommandValidatorService {

    private final Map<String, String> args;

    public CommandValidatorService(Map<String, String> args) {
        this.args = args;
    }

    public Period validateAndGetPeriod() {
        return new PeriodValidator().validateAndGet(args);
    }

    public CommandType validateAndGetCommandType() {
        return new CommandTypeValidator().validateAndGet(args);
    }

    public List<CurrencyTypes> validateAndGetCurrency() {
        return new CurrencyValidator().validateAndGet(args);
    }

    public CurrencyForecaster validateAndGetAlgorithm() {
        return new AlgorithmValidator().validateAndGet(args);
    }

    public Boolean validateAndGetOutput() {
        return new OutputValidator().validateAndGet(args);
    }
}
