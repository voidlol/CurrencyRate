package ru.liga.data;

import ru.liga.currencies.CurrencyRate;
import ru.liga.currencies.CurrencyTypes;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Reads CSV file using scanner;
 */

public class CSVParser implements CurrencyParser {

    private final Map<String, CurrencyTypes> localizedCurrencyNames = new HashMap<>();
    private static final String FILENAME_SUFFIX = "_F01_02_2002_T01_02_2022.csv";

    public CSVParser() {
        localizedCurrencyNames.put("Евро", CurrencyTypes.EUR);
        localizedCurrencyNames.put("Турецкая лира", CurrencyTypes.TRY);
        localizedCurrencyNames.put("Доллар США", CurrencyTypes.USD);
    }

    @Override
    public List<CurrencyRate> getCurrencyRates(CurrencyTypes type) {
        List<CurrencyRate> rates = new ArrayList<>();
        InputStream is = this.getClass().getResourceAsStream("/" + type + FILENAME_SUFFIX);
        try (Scanner scanner = new Scanner(is)) {
            scanner.nextLine();
            while (scanner.hasNext()) {
                rates.add(parseCurrencyRate(scanner.nextLine()));
            }
        }

        return rates;
    }

    /**
     * Parses string to create CurrencyRate from it
     * @param line string to parse
     * @return new CurrencyRate object
     */

    private CurrencyRate parseCurrencyRate(String line) {
        Scanner scanner = new Scanner(line);
        scanner.useDelimiter(";");
        LocalDate date = LocalDate.parse(scanner.next(), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        Double rate = scanner.nextDouble();
        CurrencyTypes type = localizedCurrencyNames.get(scanner.next());
        return new CurrencyRate(date, type, rate);
    }
}
