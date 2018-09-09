package uk.co.automatictester.lightning.core.tests;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import uk.co.automatictester.lightning.core.enums.TestResult;

import java.util.Arrays;
import java.util.List;

public class RespTimeMaxTest extends AbstractRespTimeTest {

    private static final List<String> FIELDS_TO_EXCLUDE = Arrays.asList("longestTransactions", "transactionCount", "actualResultDescription", "result", "actualResult");
    private static final String TEST_TYPE = "maxRespTimeTest";
    private static final String EXPECTED_RESULT_MESSAGE = "Max response time <= %s";
    private static final String ACTUAL_RESULT_MESSAGE = "Max response time = %s";
    private final long maxRespTime;

    private RespTimeMaxTest(String testName, long maxRespTime) {
        super(TEST_TYPE, testName);
        this.maxRespTime = maxRespTime;
        this.expectedResultDescription = String.format(EXPECTED_RESULT_MESSAGE, maxRespTime);
    }

    @Override
    public void calculateActualResultDescription() {
        actualResultDescription = String.format(ACTUAL_RESULT_MESSAGE, actualResult);
    }

    @Override
    protected int calculateNumericResult(DescriptiveStatistics ds) {
        return (int) ds.getMax();
    }

    @Override
    protected void calculateTestResult() {
        result = ((long) actualResult > this.maxRespTime) ? TestResult.FAIL : TestResult.PASS;
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
        return String.format("Type: %s, name: %s, threshold: %d, transaction: %s, description: %s, regexp: %s", TEST_TYPE, name, maxRespTime, transactionName, description, regexp);
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
