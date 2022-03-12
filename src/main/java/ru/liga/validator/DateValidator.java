package ru.liga.validator;

import ru.liga.exception.InvalidRangeException;
import ru.liga.type.ErrorMessages;
import ru.liga.type.RangeTypes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;

public class DateValidator implements Validator<LocalDate> {

    private static final String DATE_FORMAT_PATTER = "dd.MM.yyyy";

    @Override
    public LocalDate validateAndGet(Map<String, String> args) {
        if (!args.containsKey(CommandOptions.DATE.getKey()) && !args.containsKey(CommandOptions.PERIOD.getKey())) {
            throw new InvalidRangeException(ErrorMessages.INVALID_NO_DATE_OR_PERIOD.getText());
        } else if (args.containsKey(CommandOptions.DATE.getKey()) && args.containsKey(CommandOptions.PERIOD.getKey())) {
            throw new InvalidRangeException(ErrorMessages.INVALID_DATE_AND_PERIOD.getText());
        } else if (args.containsKey(CommandOptions.DATE.getKey())) {
            String dateValue = args.get(CommandOptions.DATE.getKey());
            if (args.containsKey(CommandOptions.OUTPUT.getKey())) {
                throw new InvalidRangeException(ErrorMessages.INVALID_NO_PERIOD_WHEN_OUTPUT_GRAPH.getText());
            }
            return getDateForDate(dateValue);
        } else {
            String periodValue = args.get(CommandOptions.PERIOD.getKey());
            return getDateForPeriod(periodValue);
        }
    }

    private LocalDate getDateForDate(String dateValue) {
        try {
            return LocalDate.parse(dateValue, DateTimeFormatter.ofPattern(DATE_FORMAT_PATTER));
        } catch (DateTimeParseException e) {
            if (dateValue.equals("tomorrow")) {
                return LocalDate.now().plusDays(1);
            }
            throw new InvalidRangeException(ErrorMessages.INVALID_DATE.getText());
        }
    }

    private LocalDate getDateForPeriod(String periodValue) {
        RangeTypes rangeType = RangeTypes.findByName(periodValue);
        if (rangeType != null) {
            return LocalDate.now().plusDays(rangeType.getDays());
        } else {
            throw new InvalidRangeException(ErrorMessages.INVALID_PERIOD.getText());
        }
    }
}
