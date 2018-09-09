package uk.co.automatictester.lightning.core.state.data;

import uk.co.automatictester.lightning.core.exceptions.CSVFileNonexistentHostAndMetricException;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;
import static uk.co.automatictester.lightning.core.enums.PerfMonColumns.HOST_AND_METRIC;

public class PerfMonEntries {

    private CsvEntries entries;

    private PerfMonEntries(List<String[]> perfMonEntries) {
        entries = new CsvEntries(perfMonEntries);
    }

    public static PerfMonEntries fromList(List<String[]> perfMonEntries) {
        return new PerfMonEntries(perfMonEntries);
    }

    public int size() {
        return entries.size();
    }

    public Stream<String[]> asStream() {
        return entries.asStream();
    }

    public List<String[]> asList() {
        return entries.asList();
    }

    public PerfMonEntries entriesWith(String hostAndMetric) {
        List<String[]> list = entries.asStream()
                .filter(e -> e[HOST_AND_METRIC.getColumn()].equals(hostAndMetric))
                .collect(collectingAndThen(toList(), filteredList -> returnListOrThrowExceptionIfEmpty(filteredList, hostAndMetric)));
        return PerfMonEntries.fromList(list);
    }

    private List<String[]> returnListOrThrowExceptionIfEmpty(List<String[]> list, String hostAndMetric) {
        if (list.size() == 0) {
            throw new CSVFileNonexistentHostAndMetricException(hostAndMetric);
        }
        return list;
    }
}
