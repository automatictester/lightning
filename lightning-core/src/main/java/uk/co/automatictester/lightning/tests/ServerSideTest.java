package uk.co.automatictester.lightning.tests;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import uk.co.automatictester.lightning.data.PerfMonDataEntries;
import uk.co.automatictester.lightning.enums.ServerSideTestType;
import uk.co.automatictester.lightning.enums.TestResult;

import java.util.ArrayList;
import java.util.List;

public class ServerSideTest extends LightningTest {

    private static final String GREATER_THAN_MESSAGE = "Average value > %s";
    private static final String LESS_THAN_MESSAGE = "Average value < %s";
    private static final String BETWEEN_MESSAGE = "Average value between %s and %s";
    private static final String ACTUAL_RESULT_MESSAGE = "Average value = %s";

    private final String hostAndMetric;
    private final ServerSideTestType subtype;
    private int dataEntriesCount;
    private final long metricValueA;
    private long metricValueB;
    private String expectedResultMessage;

    public ServerSideTest(String name, String type, ServerSideTestType subtype, String description, String hostAndMetric, long metricValueA, long metricValueB) {
        this(name, type, subtype, description, hostAndMetric, metricValueA);
        this.metricValueB = metricValueB;
    }

    public ServerSideTest(String name, String type, ServerSideTestType subtype, String description, String hostAndMetric, long metricValueA) {
        super(name, type, description);
        this.subtype = subtype;
        this.hostAndMetric = hostAndMetric;
        this.metricValueA = metricValueA;
        this.expectedResultMessage = getExpectedResultMessage();
    }

    @Override
    public void execute(ArrayList<ArrayList<String>> originalDataEntries) {
        try {
            PerfMonDataEntries dataEntries = filterDataEntries((PerfMonDataEntries) originalDataEntries);
            dataEntriesCount = dataEntries.getDataEntriesCount();

            DescriptiveStatistics ds = new DescriptiveStatistics();
            for (List<String> transaction : dataEntries) {
                String elapsed = transaction.get(1);
                ds.addValue(Double.parseDouble(elapsed));
            }
            actualResult = (int) ds.getMean();
            actualResultDescription = String.format(ACTUAL_RESULT_MESSAGE, actualResult);

            if (subtype.equals(ServerSideTestType.GREATER_THAN)) {
                expectedResultDescription = String.format(expectedResultMessage, metricValueA);
                if (actualResult > metricValueA) {
                    result = TestResult.PASS;
                } else {
                    result = TestResult.FAIL;
                }
            } else if (subtype.equals(ServerSideTestType.LESS_THAN)) {
                expectedResultDescription = String.format(expectedResultMessage, metricValueA);
                if (actualResult < metricValueA) {
                    result = TestResult.PASS;
                } else {
                    result = TestResult.FAIL;
                }
            } else if (subtype.equals(ServerSideTestType.BETWEEN)) {
                expectedResultDescription = String.format(expectedResultMessage, metricValueA, metricValueB);
                if ((actualResult > metricValueA) && (actualResult < metricValueB)) {
                    result = TestResult.PASS;
                } else {
                    result = TestResult.FAIL;
                }
            }

        } catch (Exception e) {
            result = TestResult.ERROR;
            actualResultDescription = e.getMessage();
        }
    }

    public PerfMonDataEntries filterDataEntries(PerfMonDataEntries originalPerfMonDataEntries) {
        return originalPerfMonDataEntries.excludeHostAndMetricOtherThan(getHostAndMetric());
    }

    public String getTestExecutionReport() {
        return String.format("Test name:            %s%n" +
                        "Test type:            %s%n" +
                        "Test subtype:         %s%n" +
                        "%s" +
                        "Host and metric:      %s%n" +
                        "Expected result:      %s%n" +
                        "Actual result:        %s%n" +
                        "Entries count:        %s%n" +
                        "Test result:          %s%n",
                getName(),
                getType(),
                getSubType(),
                getDescriptionForReport(),
                getHostAndMetric(),
                getExpectedResultDescription(),
                getActualResultDescription(),
                getDataEntriesCount(),
                getResultForReport());
    }

    public void printTestExecutionReport() {
        System.out.println(getTestExecutionReport());
    }

    public String getHostAndMetric() {
        return hostAndMetric;
    }

    public int getDataEntriesCount() {
        return dataEntriesCount;
    }

    public String getSubType() {
        return subtype.toString();
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    private String getExpectedResultMessage() {
        if (subtype.equals(ServerSideTestType.GREATER_THAN)) {
            return GREATER_THAN_MESSAGE;
        } else if (subtype.equals(ServerSideTestType.BETWEEN)) {
            return BETWEEN_MESSAGE;
        } else {
            return LESS_THAN_MESSAGE;
        }
    }
}
