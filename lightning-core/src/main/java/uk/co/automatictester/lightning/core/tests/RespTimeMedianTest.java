package uk.co.automatictester.lightning.core.tests;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import uk.co.automatictester.lightning.core.data.JMeterTransactions;
import uk.co.automatictester.lightning.core.enums.TestResult;
import uk.co.automatictester.lightning.core.tests.base.RespTimeBasedTest;

import static uk.co.automatictester.lightning.core.enums.JMeterColumns.TRANSACTION_DURATION_INDEX;

public class RespTimeMedianTest extends RespTimeBasedTest {

    private static final String TEST_TYPE = "medianRespTimeTest";
    private static final String MESSAGE = "median response time ";
    private static final String EXPECTED_RESULT_MESSAGE = MESSAGE + "<= %s";
    private static final String ACTUAL_RESULT_MESSAGE = MESSAGE + "= %s";

    private final double maxRespTime;

    private RespTimeMedianTest(String testName, long maxRespTime) {
        super(TEST_TYPE, testName);
        this.maxRespTime = maxRespTime;
        expectedResultDescription = String.format(EXPECTED_RESULT_MESSAGE, maxRespTime);
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
        return TEST_TYPE.hashCode() + name.hashCode() + (int) maxRespTime;
    }

    protected void calculateActualResult(JMeterTransactions jmeterTransactions) {
        DescriptiveStatistics ds = new DescriptiveStatistics();
        for (String[] transaction : jmeterTransactions.getEntries()) {
            String elapsed = transaction[TRANSACTION_DURATION_INDEX.getValue()];
            ds.addValue(Double.parseDouble(elapsed));
        }
        actualResult = (int) ds.getPercentile(50);
    }

    protected void calculateTestResult() {
        if (actualResult > maxRespTime) {
            result = TestResult.FAIL;
        } else {
            result = TestResult.PASS;
        }
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

        public RespTimeMedianTest.Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public RespTimeMedianTest.Builder withTransactionName(String transactionName) {
            this.transactionName = transactionName;
            return this;
        }

        public RespTimeMedianTest.Builder withRegexp() {
            this.regexp = true;
            return this;
        }

        public RespTimeMedianTest build() {
            RespTimeMedianTest test = new RespTimeMedianTest(testName, maxRespTime);
            test.description = this.description;
            test.transactionName = this.transactionName;
            test.regexp = this.regexp;
            return test;
        }
    }
}
