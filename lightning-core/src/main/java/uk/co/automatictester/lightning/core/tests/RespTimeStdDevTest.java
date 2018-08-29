package uk.co.automatictester.lightning.core.tests;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import uk.co.automatictester.lightning.core.data.JMeterTransactions;
import uk.co.automatictester.lightning.core.enums.TestResult;
import uk.co.automatictester.lightning.core.tests.base.ClientSideTest;

import java.util.stream.Collectors;

import static uk.co.automatictester.lightning.core.enums.JMeterColumns.TRANSACTION_DURATION_INDEX;

public class RespTimeStdDevTest extends ClientSideTest {

    private static final String TEST_TYPE = "respTimeStdDevTest";
    private static final String EXPECTED_RESULT_MESSAGE = "Average standard deviance time <= %s";
    private static final String ACTUAL_RESULT_MESSAGE = "Average standard deviance time = %s";

    private final long maxRespTimeStdDev;

    private RespTimeStdDevTest(String testName, long maxRespTimeStdDev) {
        super(TEST_TYPE, testName);
        this.maxRespTimeStdDev = maxRespTimeStdDev;
        this.expectedResultDescription = String.format(EXPECTED_RESULT_MESSAGE, maxRespTimeStdDev);
    }

    @Override
    public void calculateActualResultDescription() {
        actualResultDescription = String.format(ACTUAL_RESULT_MESSAGE, actualResult);
    }

    @Override
    protected void calculateActualResult(JMeterTransactions jmeterTransactions) {
        DescriptiveStatistics ds = new DescriptiveStatistics();
        jmeterTransactions.getEntries().stream()
                .map(t -> Double.parseDouble(t[TRANSACTION_DURATION_INDEX.getValue()]))
                .forEach(ds::addValue);
        actualResult = (int) ds.getStandardDeviation();
    }

    @Override
    protected void calculateTestResult() {
        if (actualResult > maxRespTimeStdDev) {
            result = TestResult.FAIL;
        } else {
            result = TestResult.PASS;
        }
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return TEST_TYPE.hashCode() + name.hashCode() + (int) maxRespTimeStdDev;
    }

    public static class Builder {
        private String testName;
        private long maxRespTimeStdDev;
        private String description;
        private String transactionName;
        private boolean regexp = false;

        public Builder(String testName, long maxRespTimeStdDev) {
            this.testName = testName;
            this.maxRespTimeStdDev = maxRespTimeStdDev;
        }

        public RespTimeStdDevTest.Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public RespTimeStdDevTest.Builder withTransactionName(String transactionName) {
            this.transactionName = transactionName;
            return this;
        }

        public RespTimeStdDevTest.Builder withRegexp() {
            this.regexp = true;
            return this;
        }

        public RespTimeStdDevTest build() {
            RespTimeStdDevTest test = new RespTimeStdDevTest(testName, maxRespTimeStdDev);
            test.description = this.description;
            test.transactionName = this.transactionName;
            test.regexp = this.regexp;
            return test;
        }
    }
}
