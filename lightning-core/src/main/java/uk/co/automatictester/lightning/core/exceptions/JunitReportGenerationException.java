package uk.co.automatictester.lightning.core.exceptions;

public class JunitReportGenerationException extends RuntimeException {
    public JunitReportGenerationException(Exception e) {
        super(e);
    }
}
