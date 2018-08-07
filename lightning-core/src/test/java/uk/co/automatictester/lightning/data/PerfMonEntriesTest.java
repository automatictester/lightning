package uk.co.automatictester.lightning.data;

import org.hamcrest.CoreMatchers;
import org.testng.annotations.Test;
import uk.co.automatictester.lightning.exceptions.CSVFileIOException;
import uk.co.automatictester.lightning.exceptions.CSVFileNoTransactionsException;
import uk.co.automatictester.lightning.exceptions.CSVFileNonexistentHostAndMetricException;
import uk.co.automatictester.lightning.shared.TestData;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PerfMonEntriesTest {

    @Test
    public void verifyReadMethod() {
        PerfMonEntries perfMonEntries = PerfMonEntries.fromFile(TestData.CSV_2_ENTRIES);
        assertThat(perfMonEntries, CoreMatchers.hasItem(TestData.CPU_ENTRY_9128));
        assertThat(perfMonEntries, CoreMatchers.hasItem(TestData.CPU_ENTRY_21250));
    }

    @Test(expectedExceptions = CSVFileIOException.class)
    public void verifyReadMethodIOException() {
        PerfMonEntries.fromFile(TestData.CSV_NONEXISTENT);
    }

    @Test(expectedExceptions = CSVFileNoTransactionsException.class)
    public void verifyReadMethodNoTransactionsException() {
        PerfMonEntries.fromFile(TestData.CSV_0_ENTRIES);
    }

    @Test
    public void testExcludeHostAndMetricOtherThanMethod() {
        List<String[]> testData = new ArrayList<>();
        testData.add(new String[]{"1455366135623", "9128", "192.168.0.12 CPU"});
        testData.add(new String[]{"1455366145623", "1232", "192.168.0.12 CPU"});
        testData.add(new String[]{"1455366145623", "3212", "192.168.0.15 CPU"});
        PerfMonEntries perfMonEntries = PerfMonEntries.fromList(testData);

        assertThat(perfMonEntries.getEntriesWith("192.168.0.12 CPU").size(), is(2));
    }

    @Test(expectedExceptions = CSVFileNonexistentHostAndMetricException.class)
    public void testExcludeHostAndMetricOtherThanMethodException() {
        List<String[]> testData = new ArrayList<>();
        testData.add(new String[]{"1455366135623", "9128", "192.168.0.12 CPU"});
        PerfMonEntries perfMonEntries = PerfMonEntries.fromList(testData);

        perfMonEntries.getEntriesWith("192.168.0.14 CPU");
    }

}