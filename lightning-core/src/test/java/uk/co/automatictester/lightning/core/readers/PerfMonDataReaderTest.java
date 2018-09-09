package uk.co.automatictester.lightning.core.readers;

import org.testng.annotations.Test;
import uk.co.automatictester.lightning.core.exceptions.CSVFileIOException;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static uk.co.automatictester.lightning.shared.LegacyTestData.*;

public class PerfMonDataReaderTest {

    @Test
    public void verifyReadMethod() {
        CsvDataReader reader = new PerfMonDataReader();
        List<String[]> entries = reader.fromFile(CSV_2_ENTRIES);
        assertThat(entries, hasItem(CPU_ENTRY_9128));
        assertThat(entries, hasItem(CPU_ENTRY_21250));
        assertThat(entries.size(), is(2));
    }

    @Test(expectedExceptions = CSVFileIOException.class)
    public void verifyReadMethodIOException() {
        CsvDataReader reader = new PerfMonDataReader();
        reader.fromFile(CSV_NONEXISTENT);
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void verifyReadMethodNoTransactionsException() {
        CsvDataReader reader = new PerfMonDataReader();
        reader.fromFile(CSV_0_ENTRIES);
    }
}
