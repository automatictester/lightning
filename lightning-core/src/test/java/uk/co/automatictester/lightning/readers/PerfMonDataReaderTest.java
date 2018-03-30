package uk.co.automatictester.lightning.readers;

import org.hamcrest.CoreMatchers;
import org.testng.annotations.Test;
import uk.co.automatictester.lightning.data.PerfMonDataEntries;
import uk.co.automatictester.lightning.exceptions.CSVFileIOException;
import uk.co.automatictester.lightning.exceptions.CSVFileNoTransactionsException;
import uk.co.automatictester.lightning.shared.TestData;

import static org.hamcrest.MatcherAssert.assertThat;

public class PerfMonDataReaderTest {

    @Test
    public void verifyReadMethod() {
        PerfMonDataEntries perfMonDataEntries = new PerfMonDataReader().getDataEntires(TestData.CSV_2_ENTRIES);
        assertThat(perfMonDataEntries, CoreMatchers.hasItem(TestData.CPU_ENTRY_9128));
        assertThat(perfMonDataEntries, CoreMatchers.hasItem(TestData.CPU_ENTRY_21250));
    }

    @Test(expectedExceptions = CSVFileIOException.class)
    public void verifyReadMethodIOException() {
        new PerfMonDataReader().getDataEntires(TestData.CSV_NONEXISTENT);
    }

    @Test(expectedExceptions = CSVFileNoTransactionsException.class)
    public void verifyReadMethodNoTransactionsException() {
        new PerfMonDataReader().getDataEntires(TestData.CSV_0_ENTRIES);
    }

}