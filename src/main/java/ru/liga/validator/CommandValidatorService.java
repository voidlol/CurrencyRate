package ru.liga.validator;

import lombok.RequiredArgsConstructor;
import ru.liga.algorithm.CurrencyForecaster;
import ru.liga.input.Period;
import ru.liga.type.CommandType;
import ru.liga.type.CurrencyTypes;
import ru.liga.type.OutputType;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class CommandValidatorService {

    private final Map<String, String> args;

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

    public OutputType validateAndGetOutput() {
        return new OutputValidator().validateAndGet(args);
    }
}
