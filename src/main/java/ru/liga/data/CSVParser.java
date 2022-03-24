package ru.liga.data;

import lombok.extern.slf4j.Slf4j;
import ru.liga.currency.CurrencyRate;
import ru.liga.type.CurrencyTypes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Reads CSV file using BufferedReader;
 */
@Slf4j
public class CSVParser implements CurrencyParser {

    private static final String DELIMITER = ";";
    private static final Character GROUPING_SEPARATOR = '.';
    private final Map<String, CurrencyTypes> localizedCurrencyNames = new HashMap<>();
    private static final String FILENAME_SUFFIX = "_F01_02_2005_T05_03_2022.csv";
    private static final String FILE_CHARSET = "windows-1251";
    private static final List<String> HEADER_NAMES = new ArrayList<>();
    private static final String NOMINAL_HEADER = "nominal";
    private static final String DATE_HEADER = "data";
    private static final String RATE_HEADER = "curs";
    private static final String CURRENCY_NAME_HEADER = "cdx";
    private static final NumberFormat nf = NumberFormat.getInstance(new Locale("RU"));
    private static final DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance();
    private static final DecimalFormatSymbols dfs = new DecimalFormatSymbols();

    public CSVParser() {
        localizedCurrencyNames.put("Евро", CurrencyTypes.EUR);
        localizedCurrencyNames.put("Турецкая лира", CurrencyTypes.TRY);
        localizedCurrencyNames.put("Доллар США", CurrencyTypes.USD);
        localizedCurrencyNames.put("Армянский драм", CurrencyTypes.AMD);
        localizedCurrencyNames.put("Болгарский лев", CurrencyTypes.BGN);
        dfs.setGroupingSeparator(GROUPING_SEPARATOR);
        df.setDecimalFormatSymbols(dfs);
    }

    /**
     * Reads CSV file to get Currency Rates
     *
     * @param type type of currency to read
     * @return List of CurrencyRate
     */
    @Override
    public List<CurrencyRate> getCurrencyRates(CurrencyTypes type) {
        List<CurrencyRate> rates = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        this.getClass().getResourceAsStream("/" + type + FILENAME_SUFFIX), FILE_CHARSET))) {
            HEADER_NAMES.clear();
            HEADER_NAMES.addAll(Arrays.asList(br.readLine().split(DELIMITER)));
            checkHeaders();
            br.lines().skip(1).forEach(l -> rates.add(parseCurrencyRate(l)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        rates.sort(Comparator.comparing(CurrencyRate::getDate).reversed());
        return rates;
    }

    /**
     * Parses string to create CurrencyRate from it
     *
     * @param line string to parse
     * @return new CurrencyRate object
     */
    private CurrencyRate parseCurrencyRate(String line) {
        CurrencyRate currencyRate = new CurrencyRate();
        String[] currencyData = line.split(DELIMITER);
        for (int i = 0; i < HEADER_NAMES.size(); i++) {
            if (HEADER_NAMES.get(i).equals(NOMINAL_HEADER)) {
                try {
                    currencyRate.setNominal(df.parse(currencyData[i]).intValue());
                } catch (ParseException e) {
                    log.error("Wrong nominal format: {}", currencyData[i]);
                }
            } else if (HEADER_NAMES.get(i).equals(DATE_HEADER)) {
                currencyRate.setDate(LocalDate.parse(currencyData[i], DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            } else if (HEADER_NAMES.get(i).equals(RATE_HEADER)) {
                if (currencyData[i].startsWith("\"")) {
                    currencyData[i] = currencyData[i].replace("\"", "");
                }
                try {
                    currencyRate.setRate(nf.parse(currencyData[i]).doubleValue());
                } catch (ParseException e) {
                    log.error("Wrong currency format: {}", currencyData[i]);
                }
            } else if (HEADER_NAMES.get(i).equals(CURRENCY_NAME_HEADER)) {
                currencyRate.setType(localizedCurrencyNames.get(currencyData[i]));
            }
        }
        currencyRate.normalize();
        return currencyRate;
    }

    private void checkHeaders() throws IOException {
        if (!HEADER_NAMES.contains(NOMINAL_HEADER)
            || !HEADER_NAMES.contains(CURRENCY_NAME_HEADER)
            || !HEADER_NAMES.contains(DATE_HEADER)
            || !HEADER_NAMES.contains(RATE_HEADER)) {
            throw new IOException("Wrong file format.");
        }
    }
}
