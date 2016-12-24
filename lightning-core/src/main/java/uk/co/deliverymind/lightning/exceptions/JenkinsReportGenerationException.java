package uk.co.deliverymind.lightning.exceptions;

public class JenkinsReportGenerationException extends RuntimeException {
    public JenkinsReportGenerationException(Exception e) {
        super(e);
    }
}
