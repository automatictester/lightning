package uk.co.automatictester.lightning.core.state.data;

import uk.co.automatictester.lightning.core.exceptions.CSVFileNonexistentHostAndMetricException;
import uk.co.automatictester.lightning.core.readers.PerfMonBean;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

public class PerfMonEntries {

    private List<PerfMonBean> entries;

    private PerfMonEntries(List<PerfMonBean> perfMonEntries) {
        entries = new ArrayList<>(perfMonEntries);
    }

    public static PerfMonEntries fromList(List<PerfMonBean> perfMonEntries) {
        return new PerfMonEntries(perfMonEntries);
    }

    public int size() {
        return entries.size();
    }

    public Stream<PerfMonBean> asStream() {
        return entries.stream();
    }

    public List<PerfMonBean> asList() {
        return entries;
    }

    public PerfMonEntries entriesWith(String hostAndMetric) {
        List<PerfMonBean> list = entries.stream()
                .filter(e -> e.getHostAndMetric().equals(hostAndMetric))
                .collect(collectingAndThen(toList(), filteredList -> validateAndReturn(filteredList, hostAndMetric)));
        return PerfMonEntries.fromList(list);
    }

    private List<PerfMonBean> validateAndReturn(List<PerfMonBean> list, String hostAndMetric) {
        if (list.size() == 0) {
            throw new CSVFileNonexistentHostAndMetricException(hostAndMetric);
        }
        return list;
    }
}
