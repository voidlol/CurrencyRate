package ru.liga.validator;

import ru.liga.algorithm.CurrencyForecaster;
import ru.liga.exception.InvalidAlgorithmException;
import ru.liga.type.AlgorithmTypes;
import ru.liga.type.CommandOptions;
import ru.liga.type.ErrorMessages;

import java.util.Map;

public class AlgorithmValidator implements Validator<CurrencyForecaster> {

    @Override
    public CurrencyForecaster validateAndGet(Map<String, String> args) {
        if (!args.containsKey(CommandOptions.ALGORITHM.getKey())) {
            throw new InvalidAlgorithmException(ErrorMessages.INVALID_NO_ALG.getText());
        } else {
            try {
                return AlgorithmTypes.valueOf(args.get(CommandOptions.ALGORITHM.getKey()).toUpperCase()).getAlgorithm();
            } catch (IllegalArgumentException e) {
                throw new InvalidAlgorithmException(ErrorMessages.INVALID_ALGORITHM.getText());
            }
        }
    }
}
