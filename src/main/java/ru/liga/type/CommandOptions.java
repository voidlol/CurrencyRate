package ru.liga.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommandOptions {
    COMMAND("command"),
    CURRENCY("currency"),
    DATE("-date"),
    PERIOD("-period"),
    ALGORITHM("-alg"),
    OUTPUT("-output");

    private final String key;

}
