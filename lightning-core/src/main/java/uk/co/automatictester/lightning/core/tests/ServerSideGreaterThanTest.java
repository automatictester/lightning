package uk.co.automatictester.lightning.core.tests;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import uk.co.automatictester.lightning.core.enums.TestResult;

import java.util.Arrays;
import java.util.List;

public class ServerSideGreaterThanTest extends AbstractServerSideTest {

    private static final List<String> FIELDS_TO_EXCLUDE = Arrays.asList("dataEntriesCount", "actualResultDescription", "result", "actualResult");
    private static final String EXPECTED_RESULT_MESSAGE = "Average value > %s";
    private final long threshold;

    private ServerSideGreaterThanTest(String testName, long threshold) {
        super(testName);
        this.threshold = threshold;
        this.expectedResultDescription = String.format(EXPECTED_RESULT_MESSAGE, threshold);
    }

    @Override
    protected void calculateTestResult() {
        result = (actualResult > threshold) ? TestResult.PASS : TestResult.FAIL;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj, FIELDS_TO_EXCLUDE);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, FIELDS_TO_EXCLUDE);
    }

    @Override
    public String toString() {
        return String.format("Type: %s, name: %s, threshold: %d, host and metric: %s, description: %s", TEST_TYPE, name, threshold, hostAndMetric, description);
    }

    public static class Builder {
        private String hostAndMetric;
        private long threshold;
        private String testName;
        private String description;

        public Builder(String testName, long threshold) {
            this.testName = testName;
            this.threshold = threshold;
        }

        public ServerSideGreaterThanTest.Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public ServerSideGreaterThanTest.Builder withHostAndMetric(String hostAndMetric) {
            this.hostAndMetric = hostAndMetric;
            return this;
        }

        public ServerSideGreaterThanTest build() {
            ServerSideGreaterThanTest test = new ServerSideGreaterThanTest(testName, threshold);
            test.description = this.description;
            test.hostAndMetric = this.hostAndMetric;
            return test;
        }
    }
}
