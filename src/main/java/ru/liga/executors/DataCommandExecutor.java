package ru.liga.executors;

import ru.liga.currencies.CurrencyRate;
import ru.liga.currencies.CurrencyTypes;
import ru.liga.input.UserCommand;
import ru.liga.output.CommandResult;
import ru.liga.repository.CurrencyRepository;

import java.util.List;

public class DataCommandExecutor implements CommandExecutor {

    private final CurrencyRepository repository;
    private final UserCommand command;

    public DataCommandExecutor(CurrencyRepository repository, UserCommand userCommand) {
        this.repository = repository;
        this.command = userCommand;
    }

    @Override
    public CommandResult execute() {
        CurrencyTypes type = command.getCurrencyTypes().stream()
                .findFirst()
                .get();
        return new CommandResult(getForecast(type));

    }

    private List<CurrencyRate> getForecast(CurrencyTypes type) {
        return command.getAlgorithm().getForecast(repository, type, command.getTargetDate(), command.getRangeType() != null);
    }

}
