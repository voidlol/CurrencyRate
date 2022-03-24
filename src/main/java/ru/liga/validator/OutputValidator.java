package ru.liga.validator;

import ru.liga.exception.InvalidOutputException;
import ru.liga.type.CommandOptions;
import ru.liga.type.ErrorMessages;
import ru.liga.type.OutputType;

import java.util.Map;

public class OutputValidator implements Validator<OutputType> {

    @Override
    public OutputType validateAndGet(Map<String, String> args) {
        if (!args.containsKey(CommandOptions.OUTPUT.getKey())) {
            return null;
        } else {
            try {
                return OutputType.valueOf(args.get(CommandOptions.OUTPUT.getKey()).toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new InvalidOutputException(ErrorMessages.INVALID_OUTPUT.getText());
            }
        }
    }
}
