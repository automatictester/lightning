package uk.co.deliverymind.lightning.readers;

import com.univocity.parsers.common.processor.ConcurrentRowProcessor;
import com.univocity.parsers.common.processor.RowListProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.deliverymind.lightning.data.JMeterTransactions;
import uk.co.deliverymind.lightning.exceptions.CSVFileIOException;
import uk.co.deliverymind.lightning.exceptions.CSVFileNoTransactionsException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class JMeterCSVFileReader {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public JMeterTransactions getTransactions(File csvFile) {
        long start = System.currentTimeMillis();
        logger.debug("Reading CSV file - start");
        JMeterTransactions jmeterTransactions = new JMeterTransactions();

        try (FileReader fr = new FileReader(csvFile)) {
            jmeterTransactions.addAll(getParser().parseAll(fr));
        } catch (IOException e) {
            throw new CSVFileIOException(e);
        }

        if (jmeterTransactions.isEmpty()) {
            throw new CSVFileNoTransactionsException();
        }

        long finish = System.currentTimeMillis();
        long millisecondsBetween = finish - start;
        logger.debug("Reading CSV file - finish, read {} rows, took {}ms", jmeterTransactions.size(), millisecondsBetween);

        return jmeterTransactions;
    }

    private CsvParser getParser() {
        CsvParserSettings parserSettings = new CsvParserSettings();
        parserSettings.setLineSeparatorDetectionEnabled(true);
        parserSettings.setHeaderExtractionEnabled(true);
        parserSettings.selectFields("label", "elapsed", "success", "timeStamp");
        RowListProcessor rowProcessor = new RowListProcessor();
        parserSettings.setProcessor(new ConcurrentRowProcessor(rowProcessor));
        return new CsvParser(parserSettings);
    }
}
