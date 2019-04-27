package uk.co.automatictester.lightning.core.readers;

import com.univocity.parsers.csv.CsvParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.automatictester.lightning.core.exceptions.CSVFileIOException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class LocalFilesystemCsvDataReader {

    private static final Logger log = LoggerFactory.getLogger(LocalFilesystemCsvDataReader.class);
    private final CsvDataReader csvDataReader;

    public LocalFilesystemCsvDataReader(CsvDataReader csvDataReader) {
        this.csvDataReader = csvDataReader;
    }

    public List<? extends CsvBean> fromFile(File csvFile) {
        Instant start = Instant.now();
        log.debug("Reading CSV file - start");

        CsvParser parser = new CsvParser(csvDataReader.csvParserSettings());
        parser.parse(getReader(csvFile.toString()));
        List<? extends CsvBean> beans = csvDataReader.beanListProcessor().getBeans();

        Instant finish = Instant.now();
        Duration duration = Duration.between(start, finish);
        log.debug("Reading CSV file - finish, read {} rows, took {}ms", beans.size(), duration.toMillis());
        csvDataReader.throwExceptionIfEmpty(beans);
        return beans;
    }

    private static Reader getReader(String csvFile) {
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(csvFile);
        } catch (FileNotFoundException e) {
            throw new CSVFileIOException(e);
        }
        return fileReader;
    }
}
