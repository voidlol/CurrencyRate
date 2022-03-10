package ru.liga.executors;

import com.github.sh0nk.matplotlib4j.NumpyUtils;
import com.github.sh0nk.matplotlib4j.Plot;
import com.github.sh0nk.matplotlib4j.PythonExecutionException;
import ru.liga.currencies.CurrencyRate;
import ru.liga.currencies.CurrencyTypes;
import ru.liga.input.UserCommand;
import ru.liga.output.CommandResult;
import ru.liga.repository.CurrencyRepository;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GraphCommandExecutor implements CommandExecutor {

    private final CurrencyRepository repository;
    private final UserCommand command;
    private final String fileName;

    public GraphCommandExecutor(CurrencyRepository repository, UserCommand command) {
        this.repository = repository;
        this.command = command;
        this.fileName = "png/" + LocalDate.now() + command.getInputString() + ".png";
    }

    @Override
    public CommandResult execute() {
        Map<CurrencyTypes, List<CurrencyRate>> data = new EnumMap<>(CurrencyTypes.class);
        for (CurrencyTypes type: command.getCurrencyTypes()) {
            data.put(type, getForecast(type));
        }
        try {
            generateGraph(data);
        } catch (PythonExecutionException | IOException e) {
            e.printStackTrace();
        }
        return new CommandResult(new File(fileName));
    }

    private List<CurrencyRate> getForecast(CurrencyTypes type) {
        return command.getAlgorithm().getForecast(repository, type, command.getTargetDate(), true);
    }


    private void generateGraph(Map<CurrencyTypes, List<CurrencyRate>> data) throws PythonExecutionException, IOException {
        if (new File(fileName).exists()) {
            return;
        }
        int days = command.getRangeType().getDays();
        List<Double> x = NumpyUtils.linspace(1, days, days);
        Plot plt = Plot.create();
        for (List<CurrencyRate> currencyData : data.values()) {
            List<Double> rates = currencyData.stream()
                    .map(CurrencyRate::getRate)
                    .collect(Collectors.toList());
            plt.plot().add(x, rates);
        }
        plt.xlabel("Days");
        plt.ylabel("Rate");
        plt.savefig(fileName).dpi(200);
        plt.executeSilently();
    }
}
