package ru.liga.input;

import ru.liga.currencies.CurrencyTypes;
import ru.liga.exceptions.InvalidArgumentException;
import ru.liga.exceptions.InvalidCurrencyException;
import ru.liga.exceptions.InvalidRangeException;

import java.util.Scanner;

/**
 * Simple console input
 */

public class ConsoleInput implements Input {

    private final Scanner scanner = new Scanner(System.in);

    @Override
    public String getInputString() {
        String userInput = scanner.nextLine();
        String[] words = userInput.split(" ");
        if (!(words.length == 3 && words[0].equals("rate"))) {
            throw new InvalidArgumentException("Enter correct command: rate <USD | EUR | TRY> <tomorrow | week>");
        }
        if (!CurrencyTypes.getValidCurrencies().contains(words[1])) {
            throw new InvalidCurrencyException("Enter correct currency type: USD | EUR | TRY");
        }
        if (!(words[2].equals("tomorrow") || words[2].equals("week"))) {
            throw new InvalidRangeException("Enter correct range: tomorrow | week");
        }

        return userInput;
    }
}
