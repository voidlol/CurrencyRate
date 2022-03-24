package ru.liga.output;

import lombok.Getter;
import lombok.ToString;
import ru.liga.currency.CurrencyRate;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@ToString
public class CommandResult {

    private String textResult;
    private List<CurrencyRate> listResult;
    private File graphResult;
    private boolean isText;
    private boolean isFile;

    public CommandResult(List<CurrencyRate> rates) {
        textResult = rates.stream()
                .map(CurrencyRate::toString)
                .collect(Collectors.joining("\n"));
        listResult = rates;
        isText = true;
    }

    public CommandResult(File graph) {
        graphResult = graph;
        isFile = true;
    }

    public CommandResult(String text) {
        textResult = text;
        isText = true;
    }

}
