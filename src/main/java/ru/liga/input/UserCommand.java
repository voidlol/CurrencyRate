package ru.liga.input;

import ru.liga.currencies.CurrencyRate;
import ru.liga.currencies.CurrencyTypes;
import ru.liga.data.CurrencyParser;
import ru.liga.exceptions.InvalidArgumentException;
import ru.liga.exceptions.InvalidCurrencyException;
import ru.liga.exceptions.InvalidRangeException;
import ru.liga.prediction.AlgorithmTypes;
import ru.liga.prediction.CurrencyPredictor;
import ru.liga.prediction.RangeTypes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

public class UserCommand {
    private static final String DATE_ARG = "-date";
    private static final String PERIOD_ARG = "-period";
    private static final String ALG_ARG = "-alg";
    private static final String OUTPUT_ARG = "-output";
    private static final int CURRENCY_INDEX = 1;

    private final Map<String, String> arguments = new HashMap<>();
    private List<CurrencyTypes> currencyTypes;
    private LocalDate targetDate;
    private AlgorithmTypes algorithmTypes;
    private boolean isRange = false;

    public UserCommand(String userInput) {
        String[] args = userInput.split(" ");
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("-")) {
                try {
                    arguments.put(args[i++], args[i]);
                } catch (IndexOutOfBoundsException e) {
                    throw new InvalidArgumentException("Wrong command!");
                }
            }
        }
        readAlgorithm();
        readCurrencyType(args[CURRENCY_INDEX]);
        readDate();
    }

    public List<CurrencyRate> execute(CurrencyParser parser) {
        CurrencyTypes type = currencyTypes.get(0);
        CurrencyPredictor predictor = algorithmTypes.getAlgorithm();
        List<CurrencyRate> currencyRates = parser.getCurrencyRates(type, predictor.getRequiredDataSize());
        return predictor.predict(currencyRates, targetDate, isRange);
    }


    private void readDate() {
        try {
            if (arguments.containsKey(DATE_ARG) && arguments.containsKey(PERIOD_ARG)) {
                throw new InvalidArgumentException("Specify date OR period!");
            } else if (arguments.containsKey(DATE_ARG)) {
                targetDate = arguments.get(DATE_ARG).equals("tomorrow") ? LocalDate.now().plusDays(1) : LocalDate.parse(arguments.get(DATE_ARG), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                if (!targetDate.isAfter(LocalDate.now())) {
                    throw new InvalidRangeException("Date can only be a future date");
                }
            } else if (arguments.containsKey(PERIOD_ARG)) {
                targetDate = LocalDate.now().plusDays(RangeTypes.findByName(arguments.get(PERIOD_ARG)).getDays());
                isRange = true;
            } else {
                throw new InvalidArgumentException("No date or period!");
            }
        } catch (NullPointerException | DateTimeParseException e) {
            throw new InvalidRangeException("Wrong date or period!");
        }
    }

    private void readCurrencyType(String currencies) {
        currencyTypes = Arrays.stream(currencies.split(",")).map(CurrencyTypes::findByName).collect(Collectors.toList());
        if (currencyTypes.stream().anyMatch(Objects::isNull)) {
            throw new InvalidCurrencyException("Wrong currency");
        }
        if (currencyTypes.size() > 1 && currencyTypes.size() < 5 && !(arguments.containsKey(OUTPUT_ARG) && arguments.get(OUTPUT_ARG).equals("graph"))) {
            throw new InvalidArgumentException("Use -output graph when choosing more than 1 currency");
        }
    }

    private void readAlgorithm() {
        if (arguments.containsKey(ALG_ARG)) {
            algorithmTypes = AlgorithmTypes.findByName(arguments.get(ALG_ARG));
            if (algorithmTypes == null) {
                throw new InvalidArgumentException("Wrong algorithm!");
            }
        } else {
            throw new InvalidArgumentException("No algorithm!");
        }
    }
}
