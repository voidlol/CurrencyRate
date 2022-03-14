package ru.liga.type;

import lombok.Getter;

@Getter
public enum CommandOptions {
    COMMAND("command"),
    CURRENCY("currency"),
    DATE("-date"),
    PERIOD("-period"),
    ALGORITHM("-alg"),
    OUTPUT("-output");

    private String key;

    CommandOptions(String key) {
        this.key = key;
    }

}
