package uk.co.automatictester.lightning.exceptions;

public class JenkinsReportGenerationException extends RuntimeException {
    public JenkinsReportGenerationException(Exception e) {
        super(e);
    }
}
