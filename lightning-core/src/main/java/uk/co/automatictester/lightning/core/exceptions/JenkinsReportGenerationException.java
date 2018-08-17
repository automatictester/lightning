package uk.co.automatictester.lightning.core.exceptions;

public class JenkinsReportGenerationException extends RuntimeException {
    public JenkinsReportGenerationException(Exception e) {
        super(e);
    }
}
