package ru.liga.controllers;

import ru.liga.currencies.CurrencyRate;
import ru.liga.currencies.CurrencyTypes;
import ru.liga.data.CurrencyParser;
import ru.liga.prediction.CurrencyPredictor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserInputController {

    private static final Map<String, Integer> predictionRange = new HashMap<>();

    static {
        predictionRange.put("tomorrow", 1);
        predictionRange.put("week", 7);
    }

    private UserInputController() {}

    /**
     * Executes user's command
     * @param userInput command to execute
     * @param parser CurrencyParser to get currency data
     * @param predictor CurrencyPredictor to predict rates
     * @return List of strings with predicted rates
     */

    public static List<String> proceed(String userInput, CurrencyParser parser, CurrencyPredictor predictor) {
        String[] arguments = userInput.split(" ");
        CurrencyTypes type = CurrencyTypes.valueOf(arguments[1]);
        List<CurrencyRate> currencyRates = parser.getCurrencyRates(type);
        return predictor.predict(currencyRates, predictionRange.get(arguments[2]));
    }
}
