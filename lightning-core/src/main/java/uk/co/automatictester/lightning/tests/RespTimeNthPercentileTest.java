package uk.co.automatictester.lightning.tests;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.rank.Percentile;
import uk.co.automatictester.lightning.data.JMeterTransactions;
import uk.co.automatictester.lightning.enums.TestResult;
import uk.co.automatictester.lightning.utils.IntToOrdConverter;

import java.util.ArrayList;

import static uk.co.automatictester.lightning.constants.JMeterColumns.TRANSACTION_DURATION_INDEX;

public class RespTimeNthPercentileTest extends RespTimeBasedTest {

    private static final String MESSAGE = "%s percentile of transactions have response time ";
    private static final String EXPECTED_RESULT_MESSAGE = MESSAGE + "<= %s";
    private static final String ACTUAL_RESULT_MESSAGE = MESSAGE + "= %s";

    private final long maxRespTime;
    private final int percentile;

    public RespTimeNthPercentileTest(String name, String type, String description, String transactionName, int percentile, long maxRespTime) {
        super(name, type, description, transactionName);
        this.maxRespTime = maxRespTime;
        this.percentile = percentile;
        expectedResultDescription = String.format(EXPECTED_RESULT_MESSAGE, new IntToOrdConverter().convert(percentile), maxRespTime);
    }

    @Override
    public void execute(ArrayList<String[]> originalJMeterTransactions) {
        try {
            JMeterTransactions transactions = filterTransactions((JMeterTransactions) originalJMeterTransactions);
            transactionCount = transactions.size();
            calculateActualResult(transactions);
            longestTransactions = transactions.getLongestTransactions();
            actualResultDescription = String.format(ACTUAL_RESULT_MESSAGE, new IntToOrdConverter().convert(percentile), actualResult);
            calculateTestResult();
        } catch (Exception e) {
            result = TestResult.ERROR;
            actualResultDescription = e.getMessage();
        }
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    private void calculateActualResult(JMeterTransactions jmeterTransactions) {
        DescriptiveStatistics ds = new DescriptiveStatistics();
        ds.setPercentileImpl(new Percentile().withEstimationType(Percentile.EstimationType.R_3));
        for (String[] transaction : jmeterTransactions) {
            String elapsed = transaction[TRANSACTION_DURATION_INDEX];
            ds.addValue(Double.parseDouble(elapsed));
        }
        actualResult = (int) ds.getPercentile((double) percentile);
    }

    private void calculateTestResult() {
        if (actualResult > maxRespTime) {
            result = TestResult.FAIL;
        } else {
            result = TestResult.PASS;
        }
    }
}
