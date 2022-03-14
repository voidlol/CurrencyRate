package ru.liga.validator;

import ru.liga.exception.InvalidArgumentException;
import ru.liga.type.CommandOptions;
import ru.liga.type.ErrorMessages;

import java.util.Map;

public class CommandValidator implements Validator<Boolean> {

    @Override
    public Boolean validateAndGet(Map<String, String> args) {
        if (args.get(CommandOptions.COMMAND.getKey()).equalsIgnoreCase("rate")) {
            return true;
        } else {
            throw new InvalidArgumentException(ErrorMessages.INVALID_COMMAND.getText());
        }
    }
}
