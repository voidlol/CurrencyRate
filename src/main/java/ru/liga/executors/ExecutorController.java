package ru.liga.executors;

import ru.liga.input.UserCommand;
import ru.liga.repository.CurrencyRepository;

public class ExecutorController {

    private final CurrencyRepository repository;

    public ExecutorController(CurrencyRepository repository) {
        this.repository = repository;
    }

    public CommandExecutor getExecutor(UserCommand command) {
        if (command.isGraph()) {
            return new GraphCommandExecutor(repository, command);
        } else {
            return new DataCommandExecutor(repository, command);
        }
    }
}
