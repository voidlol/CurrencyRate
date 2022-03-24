package ru.liga.input;

import ru.liga.exception.InvalidArgumentException;
import ru.liga.type.CommandOptions;
import ru.liga.type.ErrorMessages;

import java.util.HashMap;
import java.util.Map;

public class InputStringParser {

    private static final int COMMAND_INDEX = 0;
    private static final int CURRENCY_INDEX = 1;
    private static final String DELIMITER = "\\s";
    private static final String KEY_PREFIX = "-";

    private InputStringParser() {
    }

    public static Map<String, String> parse(String inputString) {
        Map<String, String> args = new HashMap<>();
        String[] words = inputString.split(DELIMITER);
        if (words.length % 2 != 0) {
            throw new InvalidArgumentException(ErrorMessages.INVALID_INPUT_FORMAT.getText());
        }
        args.put(CommandOptions.COMMAND.getKey(), words[COMMAND_INDEX]);
        args.put(CommandOptions.CURRENCY.getKey(), words[CURRENCY_INDEX]);
        try {
            for (int i = CURRENCY_INDEX + 1; i < words.length; i++) {
                if (words[i].startsWith(KEY_PREFIX)) {
                    if (args.containsKey(words[i])) {
                        throw new InvalidArgumentException(
                                String.format(ErrorMessages.INVALID_SAME_KEY_MULTIPLE_TIMES.getText(), words[i]));
                    }
                    args.put(words[i], words[++i]);
                }
            }
        } catch (IndexOutOfBoundsException e) {
            throw new InvalidArgumentException(ErrorMessages.INVALID_INPUT_FORMAT.getText());
        }
        return args;
    }
}
