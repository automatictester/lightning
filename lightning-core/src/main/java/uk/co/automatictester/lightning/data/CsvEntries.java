package uk.co.automatictester.lightning.data;

import uk.co.automatictester.lightning.exceptions.CSVFileNoTransactionsException;

import java.util.ArrayList;
import java.util.List;

public abstract class CsvEntries extends ArrayList<String[]> {

    protected CsvEntries() {
    }

    protected CsvEntries(List<String[]> entries) {
        this.addAll(entries);
    }

    protected void throwExceptionIfEmpty() {
        if (this.isEmpty()) {
            throw new CSVFileNoTransactionsException();
        }
    }
}
