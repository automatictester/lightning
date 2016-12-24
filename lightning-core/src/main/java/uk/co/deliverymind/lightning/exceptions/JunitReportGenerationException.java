package uk.co.deliverymind.lightning.exceptions;

public class JunitReportGenerationException extends RuntimeException {
    public JunitReportGenerationException(Exception e) {
        super(e);
    }
}
