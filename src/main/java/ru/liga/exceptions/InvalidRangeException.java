package ru.liga.exceptions;

public class InvalidRangeException extends RuntimeException {
    public InvalidRangeException(String message) {
        super(message);
    }
}
