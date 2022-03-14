package ru.liga.executor;

import ru.liga.currency.CurrencyRate;
import ru.liga.type.CurrencyTypes;
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
        return new CommandResult(getForecast(command.getCurrencyTypes().get(0)));

    }

    private List<CurrencyRate> getForecast(CurrencyTypes type) {
        return command.getAlgorithm().getForecast(repository, type, command.getPeriod());
    }

}
