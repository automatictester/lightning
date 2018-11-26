package uk.co.automatictester.lightning.core.readers;

import com.univocity.parsers.csv.CsvParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.automatictester.lightning.core.exceptions.CSVFileIOException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class LocalFilesystemCsvDataReader {

    private static final Logger log = LoggerFactory.getLogger(LocalFilesystemCsvDataReader.class);
    private final CsvDataReader csvDataReader;

    public LocalFilesystemCsvDataReader(CsvDataReader csvDataReader) {
        this.csvDataReader = csvDataReader;
    }

    public List<String[]> fromFile(File perfMonCsvFile) {
        Instant start = Instant.now();
        log.debug("Reading CSV file - start");

        List<String[]> entries = new ArrayList<>();
        csvDataReader.csvParserSettings().setInputBufferSize(20_000_000);
        CsvParser csvParser = new CsvParser(csvDataReader.csvParserSettings());
        try (FileReader fr = new FileReader(perfMonCsvFile)) {
            entries.addAll(csvParser.parseAll(fr));
        } catch (IOException e) {
            throw new CSVFileIOException(e);
        }
        csvDataReader.throwExceptionIfEmpty(entries);

        Instant finish = Instant.now();
        Duration duration = Duration.between(start, finish);
        log.debug("Reading CSV file - finish, read {} rows, took {}ms", entries.size(), duration.toMillis());
        return entries;
    }
}
