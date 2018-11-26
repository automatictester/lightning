package uk.co.automatictester.lightning.core.readers;

import org.testng.annotations.Test;
import uk.co.automatictester.lightning.core.exceptions.CSVFileIOException;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static uk.co.automatictester.lightning.shared.LegacyTestData.*;

public class LocalFilesystemCsvDataReaderTest {

    @Test
    public void verifyReadMethod() {
        CsvDataReader perfMonReader = new PerfMonDataReader();
        LocalFilesystemCsvDataReader localReader = new LocalFilesystemCsvDataReader(perfMonReader);
        List<String[]> entries = localReader.fromFile(CSV_2_ENTRIES);
        assertThat(entries, hasItem(CPU_ENTRY_9128));
        assertThat(entries, hasItem(CPU_ENTRY_21250));
        assertThat(entries.size(), is(2));
    }

    @Test(expectedExceptions = CSVFileIOException.class)
    public void verifyReadMethodIOException() {
        CsvDataReader perfMonReader = new PerfMonDataReader();
        LocalFilesystemCsvDataReader localReader = new LocalFilesystemCsvDataReader(perfMonReader);
        localReader.fromFile(CSV_NONEXISTENT);
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void verifyReadMethodNoTransactionsException() {
        CsvDataReader perfMonReader = new PerfMonDataReader();
        LocalFilesystemCsvDataReader localReader = new LocalFilesystemCsvDataReader(perfMonReader);
        localReader.fromFile(CSV_0_ENTRIES);
    }

    @Test
    public void testFromFile() {
        CsvDataReader jmeterReader = new JmeterDataReader();
        LocalFilesystemCsvDataReader localReader = new LocalFilesystemCsvDataReader(jmeterReader);
        List<String[]> entries = localReader.fromFile(CSV_2_TRANSACTIONS);
        assertThat(entries, hasItem(LOGIN_3514_SUCCESS));
        assertThat(entries, hasItem(SEARCH_11221_SUCCESS));
        assertThat(entries.size(), is(2));
    }

    @Test(expectedExceptions = CSVFileIOException.class)
    public void testFromFileIOException() {
        CsvDataReader jmeterReader = new JmeterDataReader();
        LocalFilesystemCsvDataReader localReader = new LocalFilesystemCsvDataReader(jmeterReader);
        localReader.fromFile(CSV_NONEXISTENT);
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testFromFileNoTransactionsException() {
        CsvDataReader jmeterReader = new JmeterDataReader();
        LocalFilesystemCsvDataReader localReader = new LocalFilesystemCsvDataReader(jmeterReader);
        localReader.fromFile(CSV_0_TRANSACTIONS);
    }
}
