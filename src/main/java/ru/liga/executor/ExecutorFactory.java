package ru.liga.executor;

import lombok.AllArgsConstructor;
import ru.liga.input.UserCommand;
import ru.liga.repository.CurrencyRepository;
import ru.liga.type.OutputType;

@AllArgsConstructor
public class ExecutorFactory {

    private final CurrencyRepository repository;

    public CommandExecutor getExecutor(UserCommand command) {
        if (command.getOutputType() == OutputType.GRAPH) {
            return new GraphCommandExecutor(repository, command);
        } else {
            return new DataCommandExecutor(repository, command);
        }
    }
}
