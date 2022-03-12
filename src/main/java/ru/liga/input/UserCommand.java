package ru.liga.input;

import lombok.Getter;
import lombok.Setter;
import ru.liga.type.CurrencyTypes;
import ru.liga.algorithm.CurrencyForecaster;
import ru.liga.type.RangeTypes;
import ru.liga.validator.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Setter
@Getter
public class UserCommand {

    private LocalDate targetDate;
    private CurrencyForecaster algorithm;
    private List<CurrencyTypes> currencyTypes;
    private RangeTypes rangeType;
    private boolean isGraph;
    private String inputString;

    private UserCommand(String inputString) {
        this.inputString = inputString;
    }

    private UserCommand() {

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

    public static UserCommandBuilder getBuilder(String inputString) {
        return new UserCommand().new UserCommandBuilder(inputString);
    }

    public class UserCommandBuilder {

        private final String inputString;
        private final Validator<Boolean> commandValidator = new CommandValidator();
        private final Validator<List<CurrencyTypes>> currencyValidator = new CurrencyValidator();
        private final Validator<LocalDate> periodValidator = new DateValidator();
        private final Validator<CurrencyForecaster> algorithmValidator = new AlgorithmValidator();
        private final Validator<Boolean> outputValidator = new OutputValidator();

        private UserCommandBuilder(String inputString) {
            this.inputString = inputString;
        }

        public UserCommand build() {
            UserCommand userCommand = new UserCommand(inputString);
            Map<String, String> args = InputStringParser.parse(inputString);
            commandValidator.validateAndGet(args);
            userCommand.setTargetDate(periodValidator.validateAndGet(args));
            userCommand.setAlgorithm(algorithmValidator.validateAndGet(args));
            userCommand.setCurrencyTypes(currencyValidator.validateAndGet(args));
            userCommand.setGraph(outputValidator.validateAndGet(args));
            userCommand.setRangeType(RangeTypes.findByName(args.get(CommandOptions.PERIOD.getKey())));
            return userCommand;
        }
    }
}
