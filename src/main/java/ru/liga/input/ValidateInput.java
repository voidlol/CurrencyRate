package ru.liga.input;

import ru.liga.currencies.CurrencyTypes;

/**
 * Input wrapper, which provides input validation
 */

public class ValidateInput implements Input {

    private final Input input;

    public ValidateInput(Input input) {
        this.input = input;
    }

    /**
     * Validates input
     * @param message message to display when asking for input
     * @return input string
     */

    @Override
    public String getInputString(String message) {
        String userInput = this.input.getInputString(message);
        boolean isValidInput = false;
        while (!isValidInput) {
            String[] words = userInput.split(" ");
            if (words.length == 3
                    && words[0].equals("rate")
                    && CurrencyTypes.getValidCurrencies().contains(words[1])
                    &&
                    (words[2].equals("tomorrow") || words[2].equals("week"))) {
                isValidInput = true;
            } else {
                userInput = this.input.getInputString("Enter correct command!");
            }
        }
        return userInput;
    }
}
