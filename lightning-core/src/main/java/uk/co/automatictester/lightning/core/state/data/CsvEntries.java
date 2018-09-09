package uk.co.automatictester.lightning.core.state.data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class CsvEntries {

    protected List<String[]> entries = new ArrayList<>();

    public CsvEntries(List<String[]> entries) {
        this.entries.addAll(entries);
    }

    public List<String[]> asList() {
        return entries;
    }

    public Stream<String[]> asStream() {
        return entries.stream();
    }

    public int size() {
        return entries.size();
    }
}
