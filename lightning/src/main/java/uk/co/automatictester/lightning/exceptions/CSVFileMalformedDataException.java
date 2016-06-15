package uk.co.automatictester.lightning.exceptions;

public class CSVFileMalformedDataException extends RuntimeException {
    public CSVFileMalformedDataException() {
        super("There are more columns in file header than in its data part");
    }
}
