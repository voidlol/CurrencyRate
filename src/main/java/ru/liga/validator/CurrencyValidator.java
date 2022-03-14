package ru.liga.validator;

import ru.liga.exception.InvalidCurrencyException;
import ru.liga.type.CommandOptions;
import ru.liga.type.CurrencyTypes;
import ru.liga.type.ErrorMessages;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class CurrencyValidator implements Validator<List<CurrencyTypes>> {

    private static final int CURRENCIES_LIMIT = 5;

    @Override
    public List<CurrencyTypes> validateAndGet(Map<String, String> args) {
        if (!args.containsKey(CommandOptions.CURRENCY.getKey())) {
            throw new InvalidCurrencyException(ErrorMessages.INVALID_CURRENCY.getText());
        } else {
            List<String> currencyStrings = Arrays.asList(args.get(CommandOptions.CURRENCY.getKey()).split(","));
            if (currencyStrings.size() > CURRENCIES_LIMIT) {
                throw new InvalidCurrencyException(ErrorMessages.INVALID_CURRENCY_AMOUNT.getText());
            }
            List<CurrencyTypes> currencyTypes = currencyStrings.stream()
                    .map(CurrencyTypes::findByName)
                    .distinct()
                    .collect(Collectors.toList());
            boolean wrongCurrency = currencyTypes.stream()
                    .anyMatch(Objects::isNull);
            if (wrongCurrency) {
                throw new InvalidCurrencyException(ErrorMessages.INVALID_CURRENCY.getText());
            } else if (currencyStrings.size() != currencyTypes.size()) {
                throw new InvalidCurrencyException(ErrorMessages.INVALID_SAME_CURRENCY.getText());
            }
            if (currencyTypes.size() > 1 && !args.containsKey(CommandOptions.OUTPUT.getKey())) {
                throw new InvalidCurrencyException(ErrorMessages.INVALID_NO_OUTPUT.getText());
            }
            return currencyTypes;
        }
    }
}
