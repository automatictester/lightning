package uk.co.automatictester.lightning.readers;

import com.opencsv.CSVReader;
import uk.co.automatictester.lightning.data.PerfMonDataEntries;
import uk.co.automatictester.lightning.exceptions.CSVFileIOException;
import uk.co.automatictester.lightning.exceptions.CSVFileNoTransactionsException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class PerfMonDataReader {

    private static final int TIMESTAMP = 0;
    private static final int VALUE = 1;
    private static final int HOST_AND_METRIC = 2;

    public PerfMonDataEntries getDataEntires(String csvFile) {
        PerfMonDataEntries perfMonDataEntries = new PerfMonDataEntries();
        try {
            CSVReader reader = new CSVReader(new FileReader(csvFile));

            String[] perfMonDataEntry;
            String timestamp = null;
            String value = null;
            String hostAndMetric = null;

            while ((perfMonDataEntry = reader.readNext()) != null) {
                ArrayList<String> currentDataEntry = new ArrayList<>();
                timestamp = perfMonDataEntry[TIMESTAMP];
                value = perfMonDataEntry[VALUE];
                hostAndMetric = perfMonDataEntry[HOST_AND_METRIC];
                currentDataEntry.add(timestamp);
                currentDataEntry.add(value);
                currentDataEntry.add(hostAndMetric);
                perfMonDataEntries.add(currentDataEntry);
            }
        } catch (IOException e) {
            throw new CSVFileIOException(e.getMessage());
        }
        if (perfMonDataEntries.isEmpty()) {
            throw new CSVFileNoTransactionsException();
        }
        return perfMonDataEntries;
    }
}
