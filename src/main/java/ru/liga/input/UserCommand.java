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
        currencyType = CurrencyTypes.findByName(words[CURRENCY_INDEX]);
        rangeType = RangeTypes.findByName(words[RANGE_INDEX]);
        if (!(words.length == COMMAND_LENGTH && command.equals("rate"))) {
            throw new InvalidArgumentException("Enter correct command: rate <USD | EUR | TRY> <tomorrow | week>");
        }
        if (currencyType == null) {
            throw new InvalidCurrencyException("Enter correct currency type: USD | EUR | TRY");
        }
        if (rangeType == null) {
            throw new InvalidRangeException("Enter correct range: tomorrow | week");
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

    public List<CurrencyRate> proceed(CurrencyParser parser, CurrencyPredictor predictor) {
        CurrencyTypes type = this.getCurrencyType();
        List<CurrencyRate> currencyRates = parser.getCurrencyRates(type, predictor.getRequiredDataSize());
        return predictor.predict(currencyRates, this.getRangeType().getDays());
    }
}
