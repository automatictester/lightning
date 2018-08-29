package uk.co.automatictester.lightning.core.tests.base;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import uk.co.automatictester.lightning.core.data.PerfMonEntries;
import uk.co.automatictester.lightning.core.enums.TestResult;
import uk.co.automatictester.lightning.core.structures.TestData;

import static uk.co.automatictester.lightning.core.enums.PerfMonColumns.VALUE_INDEX;

public abstract class ServerSideTest extends LightningTest {

    protected static final String TEST_TYPE = "serverSideTest";
    private static final String ACTUAL_RESULT_MESSAGE = "Average value = %s";
    protected String hostAndMetric;
    private int dataEntriesCount;

    protected ServerSideTest(String testName) {
        super(TEST_TYPE, testName);
    }

    @Override
    public void execute() {
        try {
            PerfMonEntries originalDataEntries = TestData.getInstance().getServerSideTestData();
            PerfMonEntries dataEntries = filterDataEntries(originalDataEntries);
            dataEntriesCount = dataEntries.size();
            calculateActualResult(dataEntries);
            calculateActualResultDescription();
            calculateTestResult();
        } catch (Exception e) {
            result = TestResult.ERROR;
            actualResultDescription = e.getMessage();
        }
    }

    @Override
    public String getTestExecutionReport() {
        return String.format("Test name:            %s%n" +
                        "Test type:            %s%n" +
                        "%s" +
                        "Host and metric:      %s%n" +
                        "Expected result:      %s%n" +
                        "Actual result:        %s%n" +
                        "Entries count:        %s%n" +
                        "Test result:          %s%n",
                getName(),
                getType(),
                getDescriptionForReport(),
                getHostAndMetric(),
                getExpectedResultDescription(),
                getActualResultDescription(),
                getDataEntriesCount(),
                getResultForReport());
    }

    protected void calculateActualResultDescription() {
        actualResultDescription = String.format(ACTUAL_RESULT_MESSAGE, actualResult);
    }

    public String getHostAndMetric() {
        return hostAndMetric;
    }

    private PerfMonEntries filterDataEntries(PerfMonEntries originalPerfMonEntries) {
        return originalPerfMonEntries.getEntriesWith(getHostAndMetric());
    }

    private int getDataEntriesCount() {
        return dataEntriesCount;
    }

    private void calculateActualResult(PerfMonEntries entries) {
        DescriptiveStatistics ds = new DescriptiveStatistics();
        entries.getEntries().stream()
                .map(e -> Double.parseDouble(e[VALUE_INDEX.getValue()]))
                .forEach(ds::addValue);
        actualResult = (int) ds.getMean();
    }

    protected abstract void calculateTestResult();
}
