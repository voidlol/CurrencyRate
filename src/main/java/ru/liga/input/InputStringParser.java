package ru.liga.input;

import ru.liga.exception.InvalidArgumentException;
import ru.liga.type.ErrorMessages;
import ru.liga.validator.CommandOptions;

import java.util.HashMap;
import java.util.Map;

public class InputStringParser {

    private static final int COMMAND_INDEX = 0;
    private static final int CURRENCY_INDEX = 1;
    private static final int MIN_ARGUMENTS = 6;
    private static final int MAX_ARGUMENTS = 8;

    private InputStringParser() {
    }

    public static Map<String, String> parse(String inputString) {
        Map<String, String> args = new HashMap<>();
        String[] words = inputString.split(" ");
        if (words.length != MAX_ARGUMENTS && words.length != MIN_ARGUMENTS) {
            throw new InvalidArgumentException(ErrorMessages.INVALID_INPUT_FORMAT.getText());
        }
        args.put(CommandOptions.COMMAND.getKey(), words[COMMAND_INDEX]);
        args.put(CommandOptions.CURRENCY.getKey(), words[CURRENCY_INDEX]);
        try {
            for (int i = CURRENCY_INDEX + 1; i < words.length; i++) {
                if (words[i].startsWith("-")) {
                    args.put(words[i++], words[i]);
                }
            }
        } catch (IndexOutOfBoundsException e) {
            throw new InvalidArgumentException(ErrorMessages.INVALID_INPUT_FORMAT.getText());
        }
        return args;
    }
}
