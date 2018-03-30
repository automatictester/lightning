package uk.co.automatictester.lightning.data;

import uk.co.automatictester.lightning.exceptions.CSVFileNonexistentHostAndMetricException;
import uk.co.deliverymind.lightning.exceptions.CSVFileNonexistentHostAndMetricException;

import java.util.ArrayList;

public class PerfMonDataEntries extends ArrayList<String[]> {

    public PerfMonDataEntries excludeHostAndMetricOtherThan(String hostAndMetric) {
        PerfMonDataEntries dataEntries = new PerfMonDataEntries();
        for (String[] dataEntry : this) {
            if (dataEntry[2].equals(hostAndMetric)) {
                dataEntries.add(dataEntry);
            }
        }
        if (dataEntries.size() == 0)
            throw new CSVFileNonexistentHostAndMetricException(hostAndMetric);
        return dataEntries;
    }

    public int getDataEntriesCount() {
        return this.size();
    }

}
