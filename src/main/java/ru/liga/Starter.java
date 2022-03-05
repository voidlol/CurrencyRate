package ru.liga;

import ru.liga.data.CSVParser;
import ru.liga.data.CurrencyParser;
import ru.liga.input.ConsoleInput;
import ru.liga.input.Input;
import ru.liga.input.UserCommand;
import ru.liga.input.ValidateInput;
import ru.liga.output.ConsoleOutput;
import ru.liga.output.Output;
import ru.liga.prediction.ArithmeticMean;
import ru.liga.prediction.CurrencyPredictor;

public class Starter {

    private final Input input;
    private final Output output;
    private final CurrencyParser parser;
    private final CurrencyPredictor predictor;

    public Starter(Input input, Output output, CurrencyParser parser, CurrencyPredictor predictor) {
        this.input = input;
        this.output = output;
        this.parser = parser;
        this.predictor = predictor;
    }

    public void init() {
        this.output.print("Welcome to Currency Rate Predictor\n" +
                "Usage: rate <USD | EUR | TRY> <tomorrow | week>\n" +
                "Enter command:\n");
        UserCommand userInput = this.input.getUserCommand();
        userInput.execute(this.parser, this.predictor).forEach(currencyRate -> this.output.print(currencyRate.toString()));
    }

    public static void main(String[] args) {
        Output output = new ConsoleOutput();
        new Starter(new ValidateInput(new ConsoleInput(), output),
                    output,
                    new CSVParser(),
                    new ArithmeticMean()).init();
    }
}
