package ru.liga.input;

import com.github.sh0nk.matplotlib4j.NumpyUtils;
import com.github.sh0nk.matplotlib4j.Plot;
import com.github.sh0nk.matplotlib4j.PythonExecutionException;
import lombok.Getter;
import lombok.Setter;
import ru.liga.currencies.CurrencyRate;
import ru.liga.currencies.CurrencyTypes;
import ru.liga.prediction.CurrencyPredictor;
import ru.liga.repository.CurrencyRepository;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Setter
@Getter
public class UserCommand {

    private LocalDate targetDate;
    private CurrencyPredictor algorithm;
    private Set<CurrencyTypes> currencyTypes;
    private boolean isGraph;
    private boolean isRange;
    private List<CurrencyRate> forecast;
    private final String fileName;
    private CurrencyRepository repository;
    private int days;

    public UserCommand(String inputString) {
        fileName = "png/" + LocalDate.now() + inputString + ".png";
    }

    public UserCommand setRepository(CurrencyRepository repository) {
        this.repository = repository;
        return this;
    }

    /**
     * Executes this command.
     */
    public void execute() {
        if (isGraph) {
            Map<CurrencyTypes, List<CurrencyRate>> data = new EnumMap<>(CurrencyTypes.class);
            for (CurrencyTypes type: currencyTypes) {
                data.put(type, algorithm.predict(repository, type, targetDate, isRange));
            }
            try {
                generateGraph(data);
            } catch (PythonExecutionException | IOException e) {
                e.printStackTrace();
            }
        } else {
            CurrencyTypes type = currencyTypes.stream().findFirst().get();
            this.forecast = algorithm.predict(repository, type, targetDate, isRange);
        }
    }

    private void generateGraph(Map<CurrencyTypes, List<CurrencyRate>> data) throws PythonExecutionException, IOException {
        List<Double> x = NumpyUtils.linspace(1, days, days);
        Plot plt = Plot.create();
        for (List<CurrencyRate> currencyData : data.values()) {
            List<Double> rates = currencyData.stream().map(CurrencyRate::getRate).collect(Collectors.toList());
            plt.plot().add(x, rates);
        }
        plt.xlabel("Date");
        plt.ylabel("Rate");
        plt.savefig(fileName).dpi(200);
        plt.executeSilently();
    }

    public List<CurrencyRate> getForecast() {
        return this.forecast;
    }

    public File getGraphFile() {
        return new File(fileName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserCommand that = (UserCommand) o;
        return isGraph() == that.isGraph() && isRange() == that.isRange() && Objects.equals(getTargetDate(), that.getTargetDate()) && Objects.equals(getAlgorithm(), that.getAlgorithm()) && Objects.equals(getCurrencyTypes(), that.getCurrencyTypes());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTargetDate(), getAlgorithm(), getCurrencyTypes(), isGraph(), isRange());
    }
}
