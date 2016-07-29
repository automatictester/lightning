package uk.co.automatictester.lightning.data;

import org.testng.annotations.Test;
import uk.co.automatictester.lightning.exceptions.CSVFileNonexistentHostAndMetricException;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PerfMonDataEntriesTest {

    @Test
    public void testExcludeHostAndMetricOtherThanMethod() {
        PerfMonDataEntries dataEntries = new PerfMonDataEntries();
        dataEntries.add(new ArrayList<>(Arrays.asList("1455366135623", "9128", "192.168.0.12 CPU")));
        dataEntries.add(new ArrayList<>(Arrays.asList("1455366145623", "1232", "192.168.0.12 CPU")));
        dataEntries.add(new ArrayList<>(Arrays.asList("1455366145623", "3212", "192.168.0.15 CPU")));

        assertThat(dataEntries.excludeHostAndMetricOtherThan("192.168.0.12 CPU").size(), is(2));
    }

    @Test(expectedExceptions = CSVFileNonexistentHostAndMetricException.class)
    public void testExcludeHostAndMetricOtherThanMethodException() {
        PerfMonDataEntries dataEntries = new PerfMonDataEntries();
        dataEntries.add(new ArrayList<>(Arrays.asList("1455366135623", "9128", "192.168.0.12 CPU")));

        dataEntries.excludeHostAndMetricOtherThan("192.168.0.14 CPU");
    }

}