package uk.co.automatictester.lightning.core.exceptions;

public class CSVFileNonexistentHostAndMetricException extends RuntimeException {
    public CSVFileNonexistentHostAndMetricException(String label) {
        super(String.format("No entry with host and metric equal to '%s' found in CSV file", label));
    }
}
