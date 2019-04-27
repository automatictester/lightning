package uk.co.automatictester.lightning.core.readers;

import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.csv.CsvParserSettings;

import java.util.List;

public interface CsvDataReader<T> {

    CsvParserSettings csvParserSettings();

    BeanListProcessor<T> beanListProcessor();

    default void throwExceptionIfEmpty(List<T> entries) {
        if (entries.isEmpty()) {
            throw new IllegalStateException("No entries found in CSV file");
        }
    }
}
