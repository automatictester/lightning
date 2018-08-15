package uk.co.automatictester.lightning.tests;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import uk.co.automatictester.lightning.enums.TestResult;
import uk.co.automatictester.lightning.tests.base.ServerSideTest;

public class ServerSideGreaterThanTest extends ServerSideTest {

    private static final String EXPECTED_RESULT_MESSAGE = "Average value > %s";

    private long threshold;

    private ServerSideGreaterThanTest(String testName, long threshold) {
        super(testName);
        this.threshold = threshold;
        this.expectedResultDescription = String.format(EXPECTED_RESULT_MESSAGE, threshold);
    }

    protected void calculateTestResult() {
        if (actualResult > threshold) {
            result = TestResult.PASS;
        } else {
            result = TestResult.FAIL;
        }
    }

    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
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
