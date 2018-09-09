package uk.co.automatictester.lightning.core.tests;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import uk.co.automatictester.lightning.core.enums.TestResult;
import uk.co.automatictester.lightning.core.state.data.JmeterTransactions;

import java.util.Arrays;
import java.util.List;

import static uk.co.automatictester.lightning.core.enums.JmeterColumns.TRANSACTION_DURATION;

public class RespTimeStdDevTest extends AbstractClientSideTest {

    private static final List<String> FIELDS_TO_EXCLUDE = Arrays.asList("transactionCount", "actualResultDescription", "result", "actualResult");
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
    protected void calculateActualResult(JmeterTransactions jmeterTransactions) {
        DescriptiveStatistics ds = new DescriptiveStatistics();
        jmeterTransactions.asStream()
                .map(t -> Double.parseDouble(t[TRANSACTION_DURATION.getColumn()]))
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
        return EqualsBuilder.reflectionEquals(this, obj, FIELDS_TO_EXCLUDE);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, FIELDS_TO_EXCLUDE);
    }

    @Override
    public String toString() {
        return String.format("Type: %s, name: %s, threshold: %d, transaction: %s, description: %s, regexp: %s", TEST_TYPE, name, maxRespTimeStdDev, transactionName, description, regexp);
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
