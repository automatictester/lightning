package uk.co.automatictester.lightning.core.tests;

import org.apache.commons.lang3.builder.EqualsBuilder;
import uk.co.automatictester.lightning.core.enums.TestResult;
import uk.co.automatictester.lightning.core.tests.base.ServerSideTest;

import java.util.Arrays;
import java.util.List;

public class ServerSideBetweenTest extends ServerSideTest {

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
        List<String> fieldsToExclude = Arrays.asList("dataEntriesCount", "actualResultDescription", "result", "actualResult");
        return EqualsBuilder.reflectionEquals(this, obj, fieldsToExclude);
    }

    @Override
    public int hashCode() {
        return TEST_TYPE.hashCode() + name.hashCode() + (int) lowerThreshold + (int) upperThreshold;
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
