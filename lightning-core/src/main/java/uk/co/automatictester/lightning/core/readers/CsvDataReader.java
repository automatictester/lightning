package uk.co.automatictester.lightning.core.readers;

import com.univocity.parsers.csv.CsvParserSettings;

import java.util.List;

public interface CsvDataReader {

    CsvParserSettings csvParserSettings();

    default void throwExceptionIfEmpty(List<String[]> entries) {
        if (entries.isEmpty()) {
            throw new IllegalStateException("No entries found in CSV file");
        }
    }
}
