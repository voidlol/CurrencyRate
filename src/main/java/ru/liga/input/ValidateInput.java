package ru.liga.input;

import ru.liga.exceptions.InvalidArgumentException;
import ru.liga.exceptions.InvalidCurrencyException;
import ru.liga.exceptions.InvalidRangeException;
import ru.liga.output.Output;

/**
 * Input wrapper, which provides input validation
 */

public class ValidateInput implements Input {

    private final Input input;
    private final Output output;

    public ValidateInput(Input input, Output output) {
        this.input = input;
        this.output = output;
    }

    /**
     * Validates input
     * @return input string
     */

    @Override
    public UserCommand getUserCommand() {
        while (true) {
            try {
                return input.getUserCommand();
            } catch (InvalidArgumentException | InvalidRangeException | InvalidCurrencyException e) {
                this.output.print(e.getMessage());
            }
        }
    }
}
