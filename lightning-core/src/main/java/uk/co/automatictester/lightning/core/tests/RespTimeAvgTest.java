package uk.co.automatictester.lightning.core.tests;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import uk.co.automatictester.lightning.core.data.JMeterTransactions;
import uk.co.automatictester.lightning.core.enums.TestResult;
import uk.co.automatictester.lightning.core.tests.base.RespTimeBasedTest;

import static uk.co.automatictester.lightning.core.enums.JMeterColumns.TRANSACTION_DURATION_INDEX;

public class RespTimeAvgTest extends RespTimeBasedTest {

    private static final String TEST_TYPE = "avgRespTimeTest";
    private static final String EXPECTED_RESULT_MESSAGE = "Average response time <= %s";
    private static final String ACTUAL_RESULT_MESSAGE = "Average response time = %s";

    private final long maxAvgRespTime;

    private RespTimeAvgTest(String testName, long maxAvgRespTime) {
        super(TEST_TYPE, testName);
        this.maxAvgRespTime = maxAvgRespTime;
        this.expectedResultDescription = String.format(EXPECTED_RESULT_MESSAGE, maxAvgRespTime);
    }

    public void calculateActualResultDescription() {
        actualResultDescription = String.format(ACTUAL_RESULT_MESSAGE, actualResult);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return TEST_TYPE.hashCode() + name.hashCode() + (int) maxAvgRespTime;
    }

    protected void calculateActualResult(JMeterTransactions jmeterTransactions) {
        DescriptiveStatistics ds = new DescriptiveStatistics();
        jmeterTransactions.getEntries().forEach(transaction -> {
            String elapsed = transaction[TRANSACTION_DURATION_INDEX.getValue()];
            ds.addValue(Double.parseDouble(elapsed));
        });
        actualResult = (int) ds.getMean();
    }

    protected void calculateTestResult() {
        if (actualResult > maxAvgRespTime) {
            result = TestResult.FAIL;
        } else {
            result = TestResult.PASS;
        }
    }

    public static class Builder {
        private String testName;
        private long maxAvgRespTime;
        private String description;
        private String transactionName;
        private boolean regexp = false;

        public Builder(String testName, long maxAvgRespTime) {
            this.testName = testName;
            this.maxAvgRespTime = maxAvgRespTime;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder withTransactionName(String transactionName) {
            this.transactionName = transactionName;
            return this;
        }

        public Builder withRegexp() {
            this.regexp = true;
            return this;
        }

        public RespTimeAvgTest build() {
            RespTimeAvgTest test = new RespTimeAvgTest(testName, maxAvgRespTime);
            test.description = this.description;
            test.transactionName = this.transactionName;
            test.regexp = this.regexp;
            return test;
        }
    }
}
