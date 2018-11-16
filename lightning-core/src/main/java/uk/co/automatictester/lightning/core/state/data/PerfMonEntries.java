package uk.co.automatictester.lightning.core.state.data;

import uk.co.automatictester.lightning.core.exceptions.CSVFileNonexistentHostAndMetricException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;
import static uk.co.automatictester.lightning.core.enums.PerfMonColumns.HOST_AND_METRIC;

public class PerfMonEntries {

    private List<String[]> entries;

    private PerfMonEntries(List<String[]> perfMonEntries) {
        entries = new ArrayList<>(perfMonEntries);
    }

    public static PerfMonEntries fromList(List<String[]> perfMonEntries) {
        return new PerfMonEntries(perfMonEntries);
    }

    public int size() {
        return entries.size();
    }

    public Stream<String[]> asStream() {
        return entries.stream();
    }

    public List<String[]> asList() {
        return entries;
    }

    public PerfMonEntries entriesWith(String hostAndMetric) {
        List<String[]> list = entries.stream()
                .filter(e -> e[HOST_AND_METRIC.getColumn()].equals(hostAndMetric))
                .collect(collectingAndThen(toList(), filteredList -> validateAndReturn(filteredList, hostAndMetric)));
        return PerfMonEntries.fromList(list);
    }

    private List<String[]> validateAndReturn(List<String[]> list, String hostAndMetric) {
        if (list.size() == 0) {
            throw new CSVFileNonexistentHostAndMetricException(hostAndMetric);
        }
        return list;
    }
}
