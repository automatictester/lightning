package uk.co.automatictester.lightning.exceptions;

public class JunitReportGenerationException extends RuntimeException {
    public JunitReportGenerationException(Exception e) {
        super(e);
    }
}
