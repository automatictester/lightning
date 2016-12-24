package uk.co.deliverymind.lightning.exceptions;

public class CSVFileIOException extends RuntimeException {
    public CSVFileIOException(Exception e) {
        super(e);
    }
}
