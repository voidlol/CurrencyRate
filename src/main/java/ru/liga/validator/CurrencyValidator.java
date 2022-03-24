package ru.liga.validator;

import ru.liga.exception.InvalidCurrencyException;
import ru.liga.exception.InvalidOutputException;
import ru.liga.type.CommandOptions;
import ru.liga.type.CurrencyTypes;
import ru.liga.type.ErrorMessages;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CurrencyValidator implements Validator<List<CurrencyTypes>> {

    private static final int CURRENCIES_LIMIT = 5;
    private static final String REQUIRED_OUTPUT = "graph";
    private static final String DELIMITER = ",+";

    @Override
    public List<CurrencyTypes> validateAndGet(Map<String, String> args) {
        if (!args.containsKey(CommandOptions.CURRENCY.getKey())) {
            throw new InvalidCurrencyException(ErrorMessages.INVALID_CURRENCY.getText());
        } else {
            List<String> currencyStrings = Arrays.asList(args.get(CommandOptions.CURRENCY.getKey()).toUpperCase().split(DELIMITER));
            if (currencyStrings.size() > CURRENCIES_LIMIT) {
                throw new InvalidCurrencyException(ErrorMessages.INVALID_CURRENCY_AMOUNT.getText());
            }
            try {
                List<CurrencyTypes> currencyTypes = currencyStrings.stream()
                        .map(CurrencyTypes::valueOf)
                        .distinct()
                        .collect(Collectors.toList());
                if (currencyStrings.size() != currencyTypes.size()) {
                    throw new InvalidCurrencyException(ErrorMessages.INVALID_SAME_CURRENCY.getText());
                }
                if (currencyTypes.size() > 1 && !hasNeededOutput(args)) {
                    throw new InvalidOutputException(ErrorMessages.INVALID_NO_GRAPH_MULTI_CURRENCIES.getText());
                }
                return currencyTypes;
            } catch (IllegalArgumentException e) {
                throw new InvalidCurrencyException(ErrorMessages.INVALID_CURRENCY.getText());
            }
        }
    }

    private boolean hasNeededOutput(Map<String, String> args) {
        if (args.containsKey(CommandOptions.OUTPUT.getKey())) {
            return args.get(CommandOptions.OUTPUT.getKey()).equals(REQUIRED_OUTPUT);
        } else {
            throw new InvalidOutputException(ErrorMessages.INVALID_NO_OUTPUT.getText());
        }
    }
}
