package uk.co.automatictester.lightning.core.tests;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.rank.Percentile;
import uk.co.automatictester.lightning.core.enums.TestResult;
import uk.co.automatictester.lightning.core.utils.Ordinals;

import java.util.Arrays;
import java.util.List;

public class RespTimeNthPercentileTest extends AbstractRespTimeTest {

    private static final List<String> FIELDS_TO_EXCLUDE = Arrays.asList("longestTransactions", "transactionCount", "actualResultDescription", "result", "actualResult");
    private static final String TEST_TYPE = "nthPercRespTimeTest";
    private static final String MESSAGE = "%s percentile of transactions have response time ";
    private static final String EXPECTED_RESULT_MESSAGE = MESSAGE + "<= %s";
    private static final String ACTUAL_RESULT_MESSAGE = MESSAGE + "= %s";
    private final long maxRespTime;
    private final int percentile;

    private RespTimeNthPercentileTest(String testName, long maxRespTime, int percentile) {
        super(TEST_TYPE, testName);
        this.maxRespTime = maxRespTime;
        this.percentile = percentile;
        this.expectedResultDescription = String.format(EXPECTED_RESULT_MESSAGE, Ordinals.fromInt(percentile), maxRespTime);
    }

    @Override
    public void calculateActualResultDescription() {
        actualResultDescription = String.format(ACTUAL_RESULT_MESSAGE, Ordinals.fromInt(percentile), actualResult);
    }

    @Override
    protected int calculateNumericResult(DescriptiveStatistics ds) {
        ds.setPercentileImpl(new Percentile().withEstimationType(Percentile.EstimationType.R_3));
        return actualResult = (int) ds.getPercentile((double) percentile);
    }

    @Override
    protected void calculateTestResult() {
        result = (actualResult > maxRespTime) ? TestResult.FAIL : TestResult.PASS;
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
        return String.format("Type: %s, name: %s, threshold: %d, percentile: %d, transaction: %s, description: %s, regexp: %s", TEST_TYPE, name, maxRespTime, percentile, transactionName, description, regexp);
    }

    public static class Builder {
        private String testName;
        private long maxRespTime;
        private int percentile;
        private String description;
        private String transactionName;
        private boolean regexp = false;

        public Builder(String testName, long maxRespTime, int percentile) {
            this.testName = testName;
            this.maxRespTime = maxRespTime;
            this.percentile = percentile;
        }

        public RespTimeNthPercentileTest.Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public RespTimeNthPercentileTest.Builder withTransactionName(String transactionName) {
            this.transactionName = transactionName;
            return this;
        }

        public RespTimeNthPercentileTest.Builder withRegexp() {
            this.regexp = true;
            return this;
        }

        public RespTimeNthPercentileTest build() {
            RespTimeNthPercentileTest test = new RespTimeNthPercentileTest(testName, maxRespTime, percentile);
            test.description = this.description;
            test.transactionName = this.transactionName;
            test.regexp = this.regexp;
            return test;
        }
    }
}
