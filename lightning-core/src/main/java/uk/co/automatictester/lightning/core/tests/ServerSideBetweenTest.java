package uk.co.automatictester.lightning.core.tests;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import uk.co.automatictester.lightning.core.enums.TestResult;

import java.util.Arrays;
import java.util.List;

public class ServerSideBetweenTest extends AbstractServerSideTest {

    private static final List<String> FIELDS_TO_EXCLUDE = Arrays.asList("dataEntriesCount", "actualResultDescription", "result", "actualResult");
    private static final String EXPECTED_RESULT_MESSAGE = "Average value between %s and %s";
    private final long lowerThreshold;
    private final long upperThreshold;

    private ServerSideBetweenTest(String testName, long lowerThreshold, long upperThreshold) {
        super(testName);
        this.lowerThreshold = lowerThreshold;
        this.upperThreshold = upperThreshold;
        this.expectedResultDescription = String.format(EXPECTED_RESULT_MESSAGE, lowerThreshold, upperThreshold);
    }

    @Override
    protected void calculateTestResult() {
        result = ((actualResult > lowerThreshold) && (actualResult < upperThreshold)) ? TestResult.PASS : TestResult.FAIL;
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
        return String.format("Type: %s, name: %s, lower threshold: %d, upper threshold: %d, host and metric: %s, description: %s", TEST_TYPE, name, lowerThreshold, upperThreshold, hostAndMetric, description);
    }

    public static class Builder {
        private String hostAndMetric;
        private long lowerThreshold;
        private long upperThreshold;
        private String testName;
        private String description;

        public Builder(String testName, long lowerThreshold, long upperThreshold) {
            this.testName = testName;
            this.lowerThreshold = lowerThreshold;
            this.upperThreshold = upperThreshold;
        }

        public ServerSideBetweenTest.Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public ServerSideBetweenTest.Builder withHostAndMetric(String hostAndMetric) {
            this.hostAndMetric = hostAndMetric;
            return this;
        }

        public ServerSideBetweenTest build() {
            ServerSideBetweenTest test = new ServerSideBetweenTest(testName, lowerThreshold, upperThreshold);
            test.description = this.description;
            test.hostAndMetric = this.hostAndMetric;
            return test;
        }
    }
}
