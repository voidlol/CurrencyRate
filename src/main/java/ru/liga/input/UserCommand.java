package ru.liga.input;

import lombok.Getter;
import lombok.Setter;
import ru.liga.currencies.CurrencyTypes;
import ru.liga.prediction.CurrencyPredictor;
import ru.liga.prediction.RangeTypes;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Setter
@Getter
public class UserCommand {

    private LocalDate targetDate;
    private CurrencyPredictor algorithm;
    private Set<CurrencyTypes> currencyTypes;
    private RangeTypes rangeType;
    private boolean isGraph;
    private String inputString;

    public UserCommand(String inputString) {
        this.inputString = inputString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserCommand that = (UserCommand) o;
        return isGraph() == that.isGraph()
                && Objects.equals(getRangeType(), that.getRangeType())
                && Objects.equals(getTargetDate(), that.getTargetDate())
                && Objects.equals(getAlgorithm(), that.getAlgorithm())
                && Objects.equals(getCurrencyTypes(), that.getCurrencyTypes());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTargetDate(), getAlgorithm(), getCurrencyTypes(), isGraph(), getRangeType());
    }
}
