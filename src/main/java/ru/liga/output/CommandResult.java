package ru.liga.output;

import lombok.Getter;
import ru.liga.currencies.CurrencyRate;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Getter
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

    @Override
    public String toString() {
        return "CommandResult{" +
                "textResult='" + textResult + '\'' +
                ", graphResult=" + graphResult +
                ", isText=" + isText +
                ", isFile=" + isFile +
                '}';
    }
}
