package uk.co.automatictester.lightning.core.state.data;

import org.testng.annotations.Test;
import uk.co.automatictester.lightning.core.exceptions.CSVFileNonexistentHostAndMetricException;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PerfMonEntriesTest {

    @Test
    public void testExcludeHostAndMetricOtherThanMethod() {
        List<String[]> testData = new ArrayList<>();
        testData.add(new String[]{"1455366135623", "9128", "192.168.0.12 CPU"});
        testData.add(new String[]{"1455366145623", "1232", "192.168.0.12 CPU"});
        testData.add(new String[]{"1455366145623", "3212", "192.168.0.15 CPU"});
        PerfMonEntries perfMonEntries = PerfMonEntries.fromList(testData);

        assertThat(perfMonEntries.entriesWith("192.168.0.12 CPU").size(), is(2));
    }

    @Test(expectedExceptions = CSVFileNonexistentHostAndMetricException.class)
    public void testExcludeHostAndMetricOtherThanMethodException() {
        List<String[]> testData = new ArrayList<>();
        testData.add(new String[]{"1455366135623", "9128", "192.168.0.12 CPU"});
        PerfMonEntries perfMonEntries = PerfMonEntries.fromList(testData);

        perfMonEntries.entriesWith("192.168.0.14 CPU");
    }
}
