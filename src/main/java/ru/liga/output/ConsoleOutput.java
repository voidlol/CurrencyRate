package ru.liga.output;

/**
 * Simple console output
 */
public class ConsoleOutput implements Output {
    @Override
    public void print(String message) {
        System.out.println(message);
    }
}
