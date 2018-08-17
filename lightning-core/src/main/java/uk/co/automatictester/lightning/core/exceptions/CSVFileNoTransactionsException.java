package uk.co.automatictester.lightning.core.exceptions;

public class CSVFileNoTransactionsException extends RuntimeException {
    public CSVFileNoTransactionsException() {
        super("No entries found in CSV file");
    }
}
