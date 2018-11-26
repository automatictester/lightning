package uk.co.automatictester.lightning.core.readers;

import org.testng.annotations.Test;
import uk.co.automatictester.lightning.core.exceptions.CSVFileIOException;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static uk.co.automatictester.lightning.shared.LegacyTestData.*;

public class JmeterDataReaderTest {

    @Test
    public void testFromFile() {
        CsvDataReader reader = new JmeterDataReader();
        List<String[]> entries = reader.fromFile(CSV_2_TRANSACTIONS);
        assertThat(entries, hasItem(LOGIN_3514_SUCCESS));
        assertThat(entries, hasItem(SEARCH_11221_SUCCESS));
        assertThat(entries.size(), is(2));
    }

    @Test(expectedExceptions = CSVFileIOException.class)
    public void testFromFileIOException() {
        CsvDataReader reader = new JmeterDataReader();
        reader.fromFile(CSV_NONEXISTENT);
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testFromFileNoTransactionsException() {
        CsvDataReader reader = new JmeterDataReader();
        reader.fromFile(CSV_0_TRANSACTIONS);
    }
}
