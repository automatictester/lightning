package uk.co.automatictester.lightning.core.tests;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import uk.co.automatictester.lightning.core.enums.TestResult;
import uk.co.automatictester.lightning.core.tests.base.RespTimeBasedTest;

public class RespTimeMaxTest extends RespTimeBasedTest {

    private static final String TEST_TYPE = "maxRespTimeTest";
    private static final String EXPECTED_RESULT_MESSAGE = "Max response time <= %s";
    private static final String ACTUAL_RESULT_MESSAGE = "Max response time = %s";

    private final long maxRespTime;

    private RespTimeMaxTest(String testName, long maxRespTime) {
        super(TEST_TYPE, testName);
        this.maxRespTime = maxRespTime;
        this.expectedResultDescription = String.format(EXPECTED_RESULT_MESSAGE, maxRespTime);
    }

    public void calculateActualResultDescription() {
        actualResultDescription = String.format(ACTUAL_RESULT_MESSAGE, actualResult);
    }

    protected int getResult(DescriptiveStatistics ds) {
        return (int) ds.getMax();
    }

    protected void calculateTestResult() {
        if ((long) actualResult > this.maxRespTime) {
            result = TestResult.FAIL;
        } else {
            result = TestResult.PASS;
        }
    }

    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    public int hashCode() {
        return TEST_TYPE.hashCode() + name.hashCode() + (int) maxRespTime;
    }

    public static class Builder {
        private String testName;
        private long maxRespTime;
        private String description;
        private String transactionName;
        private boolean regexp = false;

        public Builder(String testName, long maxRespTime) {
            this.testName = testName;
            this.maxRespTime = maxRespTime;
        }

        public RespTimeMaxTest.Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public RespTimeMaxTest.Builder withTransactionName(String transactionName) {
            this.transactionName = transactionName;
            return this;
        }

        public RespTimeMaxTest.Builder withRegexp() {
            this.regexp = true;
            return this;
        }

        public RespTimeMaxTest build() {
            RespTimeMaxTest test = new RespTimeMaxTest(testName, maxRespTime);
            test.description = this.description;
            test.transactionName = this.transactionName;
            test.regexp = this.regexp;
            return test;
        }
    }
}
