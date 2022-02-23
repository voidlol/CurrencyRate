package ru.liga.input;

import ru.liga.output.Output;

import java.util.Scanner;

/**
 * Simple console input
 */

public class ConsoleInput implements Input {

    private final Scanner scanner = new Scanner(System.in);
    private final Output output;

    public ConsoleInput(Output output) {
        this.output = output;
    }

    @Override
    public String getInputString(String message) {
        output.print(message);
        return scanner.nextLine();
    }
}
