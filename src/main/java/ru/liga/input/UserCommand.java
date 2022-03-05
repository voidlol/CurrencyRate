package ru.liga.input;

import ru.liga.currencies.CurrencyRate;
import ru.liga.currencies.CurrencyTypes;
import ru.liga.data.CurrencyParser;
import ru.liga.exceptions.InvalidArgumentException;
import ru.liga.exceptions.InvalidCurrencyException;
import ru.liga.exceptions.InvalidRangeException;
import ru.liga.prediction.CurrencyPredictor;
import ru.liga.prediction.RangeTypes;

import java.util.List;

public class UserCommand {

    private static final int COMMAND_LENGTH = 3;
    private static final int COMMAND_INDEX = 0;
    private static final int CURRENCY_INDEX = 1;
    private static final int RANGE_INDEX = 2;

    private final String command;
    private final CurrencyTypes currencyType;
    private final RangeTypes rangeType;

    public UserCommand(String userInput) {
        String[] words = userInput.split(" ");
        command = words[COMMAND_INDEX];
        if (command.equalsIgnoreCase("Exit")) {
            System.exit(0);
        }
        if (!(words.length == COMMAND_LENGTH && command.equals("rate"))) {
            String validCommand = "rate " + CurrencyTypes.getString() + " " + RangeTypes.getString();
            throw new InvalidArgumentException("Enter correct command: " + validCommand);
        }
        currencyType = CurrencyTypes.findByName(words[CURRENCY_INDEX]);
        rangeType = RangeTypes.findByName(words[RANGE_INDEX]);

        if (currencyType == null) {
            String validCurrencies = CurrencyTypes.getString();
            throw new InvalidCurrencyException("Enter correct currency type: " + validCurrencies);
        }
        if (rangeType == null) {
            String validRange = RangeTypes.getString();
            throw new InvalidRangeException("Enter correct range: " + validRange);
        }
    }


    public String getCommand() {
        return command;
    }

    public CurrencyTypes getCurrencyType() {
        return currencyType;
    }

    public RangeTypes getRangeType() {
        return rangeType;
    }

    public List<CurrencyRate> execute(CurrencyParser parser, CurrencyPredictor predictor) {
        CurrencyTypes type = this.getCurrencyType();
        List<CurrencyRate> currencyRates = parser.getCurrencyRates(type, predictor.getRequiredDataSize());
        return predictor.predict(currencyRates, this.getRangeType().getDays());
    }
}
