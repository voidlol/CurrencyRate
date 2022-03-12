package ru.liga.input;

import java.util.Scanner;

/**
 * Simple console input
 */

public class ConsoleInput implements Input {

    private final Scanner scanner = new Scanner(System.in);

    @Override
    public UserCommand getUserCommand() {
        return UserCommand.getBuilder(scanner.nextLine()).build();
    }
}
