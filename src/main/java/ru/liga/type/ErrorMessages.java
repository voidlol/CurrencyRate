package ru.liga.type;

import lombok.Getter;

@Getter
public enum ErrorMessages {
    INVALID_INPUT_FORMAT("Invalid command. Usage: rate ["
            + CurrencyTypes.getString()
            + "] [-date <DD.MM.YYYY | tomorrow>][-period + "
            + RangeTypes.getString() +"] [-alg "
            + AlgorithmTypes.getString() + "] [-output graph]"),
    INVALID_COMMAND("Available commands: <rate>"),

    INVALID_CURRENCY("Available currencies: " + CurrencyTypes.getString()),
    INVALID_SAME_CURRENCY("You can't specify same currency more than one time."),
    INVALID_CURRENCY_AMOUNT("You can't pass more than 5 currencies at once!"),

    INVALID_NO_DATE_OR_PERIOD("You must specify date or period!"),
    INVALID_DATE_AND_PERIOD("You can't choose date and period at the same time."),
    INVALID_DATE("Enter date in format DD.MM.YYYY or \"tomorrow\""),
    INVALID_PERIOD("Available period ranges: " + RangeTypes.getString()),
    INVALID_DATE_IS_BEYOND("Date is beyond!"),
    INVALID_DATE_IN_PAST("Please, select future date."),

    INVALID_NO_ALG("Use -alg <algorithm name> to specify forecast algorithm"),
    INVALID_ALGORITHM("Available algorithms: " + AlgorithmTypes.getString()),

    INVALID_NO_OUTPUT("If you specify more than 1 currency than use \"-output graph\" argument"),
    INVALID_OUTPUT("Available output options: " + "graph"),
    INVALID_NO_PERIOD_WHEN_OUTPUT_GRAPH("Use -period instead of date when using -output graph.");


    private final String text;

    ErrorMessages(String text) {
        this.text = text;
    }
}
