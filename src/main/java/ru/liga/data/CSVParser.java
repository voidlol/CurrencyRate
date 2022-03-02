package ru.liga.data;

import ru.liga.currencies.CurrencyRate;
import ru.liga.currencies.CurrencyTypes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Reads CSV file using BufferedReader;
 */
public class CSVParser implements CurrencyParser {

    private final Map<String, CurrencyTypes> localizedCurrencyNames = new HashMap<>();
    private static final String FILENAME_SUFFIX = "_F01_02_2002_T01_02_2022.csv";

    public CSVParser() {
        localizedCurrencyNames.put("Евро", CurrencyTypes.EUR);
        localizedCurrencyNames.put("Турецкая лира", CurrencyTypes.TRY);
        localizedCurrencyNames.put("Доллар США", CurrencyTypes.USD);
    }

    /**
     * Reads CSV file to get Currency Rates
     * @param type type of currency to read
     * @param dataLength - list size
     * @return List of CurrencyRate
     */
    @Override
    public List<CurrencyRate> getCurrencyRates(CurrencyTypes type, int dataLength) {
        List<CurrencyRate> rates = new ArrayList<>();
        try (InputStream is = this.getClass().getResourceAsStream("/" + type + FILENAME_SUFFIX);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            br.lines().skip(1).limit(dataLength).forEach(l -> rates.add(parseCurrencyRate(l)));
        } catch (IOException e) {
            e.printStackTrace();
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
