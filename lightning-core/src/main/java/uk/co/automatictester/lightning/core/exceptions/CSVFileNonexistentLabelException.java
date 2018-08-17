package uk.co.automatictester.lightning.core.exceptions;

public class CSVFileNonexistentLabelException extends RuntimeException {
    public CSVFileNonexistentLabelException(String label) {
        super(String.format("No transactions with label equal to '%s' found in CSV file", label));
    }
}
