package ru.liga.input;

import ru.liga.currencies.CurrencyTypes;
import ru.liga.exceptions.*;
import ru.liga.prediction.AlgorithmTypes;
import ru.liga.prediction.RangeTypes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class UserCommandParser implements Input {

    private final Map<String, String> keyValueArgs = new HashMap<>();
    private final UserCommand userCommand;
    private final String[] args;
    private static final String DATE_PATTERN = "dd.MM.yyyy";
    private static final int COMMAND_INDEX = 0;
    private static final int CURRENCY_INDEX = 1;
    private static final int MINIMUM_ARGUMENTS_NUM = 6;
    private static final int MAXIMUM_ARGUMENTS_NUM = 8;
    private static final String COMMAND_NAME = "rate";
    private static final String DATE_ARG_NAME = "-date";
    private static final String PERIOD_ARG_NAME = "-period";
    private static final String ALGORITHM_ARG_NAME = "-alg";
    private static final String OUTPUT_ARG_NAME = "-output";


    /**
     * Parses user input into UserCommand
     * If fails throws:
     * InvalidArgumentException
     * InvalidAlgorithmException
     * InvalidCurrencyException
     * InvalidOutputException
     * InvalidRangeException
     *
     * if succeed use getUserCommand()
     * @param inputString
     */
    public UserCommandParser(String inputString) {
        this.args = inputString.split(" ");
        userCommand = new UserCommand(inputString);
        validate();
    }

    public UserCommand getUserCommand() {
        return userCommand;
    }

    private void validate() {
        validateArguments();
    }

    private void validateArguments() {
        if (args.length != MINIMUM_ARGUMENTS_NUM && args.length != MAXIMUM_ARGUMENTS_NUM) {
            throw new InvalidArgumentException(ErrorMessages.INVALID_INPUT_FORMAT.getText());
        }
        validateCommand();
    }

    private void buildKeyValue() {
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("-")) {
                try {
                    keyValueArgs.put(args[i++], args[i]);
                } catch (IndexOutOfBoundsException e) {
                    throw new InvalidArgumentException(ErrorMessages.INVALID_INPUT_FORMAT.getText());
                }
            }
        }
        validateRange();
    }

    private void validateCommand() {
        if (!args[COMMAND_INDEX].equalsIgnoreCase(COMMAND_NAME)) {
            throw new InvalidArgumentException(ErrorMessages.INVALID_COMMAND.getText());
        }
        buildKeyValue();
    }

    private void validateRange() {
        if (keyValueArgs.containsKey(DATE_ARG_NAME) && keyValueArgs.containsKey(PERIOD_ARG_NAME)) {
            throw new InvalidRangeException(ErrorMessages.INVALID_DATE_AND_PERIOD.getText());
        } else if (keyValueArgs.containsKey(DATE_ARG_NAME)) {
            validateDate();
        } else if (keyValueArgs.containsKey(PERIOD_ARG_NAME)) {
            validatePeriod();
        } else {
            throw new InvalidRangeException(ErrorMessages.INVALID_NO_DATE_OR_PERIOD.getText());
        }
    }

    private void validatePeriod() {
        RangeTypes type = RangeTypes.findByName(keyValueArgs.get(PERIOD_ARG_NAME));
        if (type == null) {
            throw new InvalidRangeException(ErrorMessages.INVALID_PERIOD.getText());
        }
        userCommand.setRange(true);
        userCommand.setTargetDate(LocalDate.now().plusDays(type.getDays()));
        validateAlgorithm();
    }

    private void validateAlgorithm() {
        if (!keyValueArgs.containsKey(ALGORITHM_ARG_NAME)) {
            throw new InvalidAlgorithmException(ErrorMessages.INVALID_NO_ALG.getText());
        }
        AlgorithmTypes type = AlgorithmTypes.findByName(keyValueArgs.get(ALGORITHM_ARG_NAME));
        if (type == null) {
            throw new InvalidAlgorithmException(ErrorMessages.INVALID_ALGORITHM.getText());
        }
        userCommand.setAlgorithm(type.getAlgorithm());
        validateCurrency();
    }

    private void validateCurrency() {
        String[] currencies = args[CURRENCY_INDEX].split(",");
        if (currencies.length > 5) {
            throw new InvalidCurrencyException(ErrorMessages.INVALID_CURRENCY_AMOUNT.getText());
        } else if (currencies.length > 1) {
            if (userCommand.isRange()) {
                validateOutput();
                userCommand.setGraph(true);
            } else {
                throw new InvalidRangeException(ErrorMessages.INVALID_NO_PERIOD_MULTI_CURRENCIES.getText());
            }
        } else if (currencies.length == 1 && keyValueArgs.containsKey(OUTPUT_ARG_NAME) && keyValueArgs.get(OUTPUT_ARG_NAME).equalsIgnoreCase("graph")) {
            throw new InvalidCurrencyException(ErrorMessages.INVALID_SINGLE_CURRENCY_GRAPH.getText());
        }
        Set<CurrencyTypes> typesSet = new HashSet<>();
        for (String currency : currencies) {
            CurrencyTypes type = CurrencyTypes.findByName(currency);
            if (type == null) {
                throw new InvalidCurrencyException(ErrorMessages.INVALID_CURRENCY.getText());
            }
            if (typesSet.contains(type)) {
                throw new InvalidCurrencyException(ErrorMessages.INVALID_SAME_CURRENCY.getText());
            }
            typesSet.add(type);
        }
        userCommand.setCurrencyTypes(typesSet);
    }

    private void validateOutput() {
        if (!keyValueArgs.containsKey(OUTPUT_ARG_NAME) || !keyValueArgs.get(OUTPUT_ARG_NAME).equalsIgnoreCase("graph")) {
            throw new InvalidOutputException(ErrorMessages.INVALID_OUTPUT.getText());
        }
        userCommand.setDays(RangeTypes.findByName(keyValueArgs.get(PERIOD_ARG_NAME)).getDays());
    }

    private void validateDate() {
        String date = keyValueArgs.get(DATE_ARG_NAME);
        try {
            userCommand.setTargetDate(LocalDate.parse(date, DateTimeFormatter.ofPattern(DATE_PATTERN)));
        } catch (DateTimeParseException e) {
            if (date.equalsIgnoreCase("tomorrow")) {
                userCommand.setTargetDate(LocalDate.now().plusDays(1));
            } else {
                throw new InvalidRangeException(ErrorMessages.INVALID_DATE.getText());
            }
        }
        validateAlgorithm();
    }

}
