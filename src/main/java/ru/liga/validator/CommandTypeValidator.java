package ru.liga.validator;

import ru.liga.exception.InvalidArgumentException;
import ru.liga.type.CommandOptions;
import ru.liga.type.CommandType;
import ru.liga.type.ErrorMessages;

import java.util.Map;

public class CommandTypeValidator implements Validator<CommandType> {

    @Override
    public CommandType validateAndGet(Map<String, String> args) {
        try {
            return CommandType.valueOf(args.get(CommandOptions.COMMAND.getKey()).toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidArgumentException(ErrorMessages.INVALID_COMMAND.getText());
        }
    }
}
