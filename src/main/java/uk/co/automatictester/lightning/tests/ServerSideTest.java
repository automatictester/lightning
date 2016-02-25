package uk.co.automatictester.lightning.tests;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import uk.co.automatictester.lightning.data.PerfMonDataEntries;
import uk.co.automatictester.lightning.enums.ServerSideTestType;
import uk.co.automatictester.lightning.enums.TestResult;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

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

    public void execute(ArrayList<ArrayList<String>> originalDataEntries) {
        Locale.setDefault(Locale.ENGLISH);

        try {
            PerfMonDataEntries dataEntries = filterDataEntries((PerfMonDataEntries) originalDataEntries);
            dataEntriesCount = dataEntries.getDataEntriesCount();

            DescriptiveStatistics ds = new DescriptiveStatistics();
            for (List<String> transaction : dataEntries) {
                String elapsed = transaction.get(1);
                ds.addValue(Double.parseDouble(elapsed));
            }
            double avgRespTime = ds.getMean();

            DecimalFormat df = new DecimalFormat("#.##");
            double roundedAvgRespTime = Double.valueOf(df.format(avgRespTime));

            actualResult = String.format(ACTUAL_RESULT_MESSAGE, roundedAvgRespTime);

            if (subtype.equals(ServerSideTestType.GREATER_THAN)) {
                expectedResult = String.format(expectedResultMessage, metricValueA);
                if (roundedAvgRespTime > metricValueA) {
                    result = TestResult.PASS;
                } else {
                    result = TestResult.FAIL;
                }
            } else if (subtype.equals(ServerSideTestType.LESS_THAN)) {
                expectedResult = String.format(expectedResultMessage, metricValueA);
                if (roundedAvgRespTime < metricValueA) {
                    result = TestResult.PASS;
                } else {
                    result = TestResult.FAIL;
                }
            } else if (subtype.equals(ServerSideTestType.BETWEEN)) {
                expectedResult = String.format(expectedResultMessage, metricValueA, metricValueB);
                if ((roundedAvgRespTime > metricValueA) && (roundedAvgRespTime < metricValueB)) {
                    result = TestResult.PASS;
                } else {
                    result = TestResult.FAIL;
                }
            }

        } catch (Exception e) {
            result = TestResult.IGNORED;
            actualResult = e.getMessage();
        }
    }

    public PerfMonDataEntries filterDataEntries(PerfMonDataEntries originalPerfMonDataEntries) {
        return originalPerfMonDataEntries.excludeHostAndMetricOtherThan(getHostAndMetric());
    }

    public void printTestExecutionReport() {
        String executionReport = String.format("Test name:            %s%n" +
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
                getExpectedResult(),
                getActualResult(),
                getDataEntriesCount(),
                getResultForReport());

        System.out.println(executionReport);
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

    public boolean equals(Object obj) {
        if (obj instanceof ServerSideTest) {
            ServerSideTest test = (ServerSideTest) obj;
            return name.equals(test.name) &&
                    description.equals(test.description) &&
                    hostAndMetric.equals(test.hostAndMetric) &&
                    type.equals(test.type) &&
                    subtype.equals(test.subtype) &&
                    expectedResult.equals(test.expectedResult) &&
                    actualResult.equals(test.actualResult) &&
                    Objects.equals(result, test.result) &&
                    dataEntriesCount == test.dataEntriesCount &&
                    metricValueA == test.metricValueA &&
                    Objects.equals(metricValueB, test.metricValueB);
        } else {
            return false;
        }
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
