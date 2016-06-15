package uk.co.automatictester.lightning.exceptions;

public class CSVFileMissingColumnNameException extends RuntimeException {
    public CSVFileMissingColumnNameException(String column) {
        super(String.format("Column name '%s' not found in first row of CSV file", column));
    }
}
