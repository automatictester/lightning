package uk.co.automatictester.lightning.tests;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import uk.co.automatictester.lightning.data.JMeterTransactions;
import uk.co.automatictester.lightning.enums.TestResult;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RespTimeStdDevTest extends ClientSideTest {

    private static final String EXPECTED_RESULT_MESSAGE = "Average standard deviance time <= %s";
    private static final String ACTUAL_RESULT_MESSAGE = "Average standard deviance time = %s";

    private final long maxRespTimeStdDev;

    public RespTimeStdDevTest(String name, String type, String description, String transactionName, long maxRespTimeStdDev) {
        super(name, type, description, transactionName);
        this.maxRespTimeStdDev = maxRespTimeStdDev;
        expectedResult = String.format(EXPECTED_RESULT_MESSAGE, maxRespTimeStdDev);
    }

    public void execute(ArrayList<ArrayList<String>> originalJMeterTransactions) {
        Locale.setDefault(Locale.ENGLISH);

        try {
            JMeterTransactions transactions = filterTransactions((JMeterTransactions) originalJMeterTransactions);
            transactionCount = transactions.getTransactionCount();

            DescriptiveStatistics ds = new DescriptiveStatistics();
            for (List<String> transaction : transactions) {
                String elapsed = transaction.get(1);
                ds.addValue(Double.parseDouble(elapsed));
            }
            double actualRespTimeStdDev = ds.getStandardDeviation();
            DecimalFormat df = new DecimalFormat("#.##");
            double roundedActualRespTimeStdDev = Double.valueOf(df.format(actualRespTimeStdDev));

            actualResult = String.format(ACTUAL_RESULT_MESSAGE, roundedActualRespTimeStdDev);
            if (roundedActualRespTimeStdDev > maxRespTimeStdDev) {
                result = TestResult.FAIL;
            } else {
                result = TestResult.PASS;
            }
        } catch (Exception e) {
            result = TestResult.IGNORED;
            actualResult = e.getMessage();
        }
    }

    public boolean equals(Object obj) {
        if (obj instanceof RespTimeStdDevTest) {
            RespTimeStdDevTest test = (RespTimeStdDevTest) obj;
            return name.equals(test.name) &&
                    description.equals(test.description) &&
                    transactionName.equals(test.transactionName) &&
                    expectedResult.equals(test.expectedResult) &&
                    actualResult.equals(test.actualResult) &&
                    result == test.result &&
                    maxRespTimeStdDev == test.maxRespTimeStdDev &&
                    transactionCount == test.transactionCount &&
                    type.equals(test.type);
        } else {
            return false;
        }
    }
}
