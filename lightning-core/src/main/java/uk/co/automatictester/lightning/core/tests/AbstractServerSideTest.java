package uk.co.automatictester.lightning.core.tests;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import uk.co.automatictester.lightning.core.enums.TestResult;
import uk.co.automatictester.lightning.core.state.data.PerfMonEntries;
import uk.co.automatictester.lightning.core.state.data.TestData;

import static uk.co.automatictester.lightning.core.enums.PerfMonColumns.VALUE;

public abstract class AbstractServerSideTest extends AbstractLightningTest {

    protected static final String TEST_TYPE = "serverSideTest";
    private static final String ACTUAL_RESULT_MESSAGE = "Average value = %s";
    protected String hostAndMetric;
    private int dataEntriesCount;

    protected AbstractServerSideTest(String testName) {
        super(TEST_TYPE, testName);
    }

    @Override
    public void execute() {
        try {
            PerfMonEntries originalDataEntries = TestData.getInstance().serverSideTestData();
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
                name(),
                type(),
                descriptionForReport(),
                hostAndMetric(),
                expectedResultDescription(),
                actualResultDescription(),
                dataEntriesCount(),
                resultForReport());
    }

    protected void calculateActualResultDescription() {
        actualResultDescription = String.format(ACTUAL_RESULT_MESSAGE, actualResult);
    }

    public String hostAndMetric() {
        return hostAndMetric;
    }

    private PerfMonEntries filterDataEntries(PerfMonEntries originalPerfMonEntries) {
        return originalPerfMonEntries.entriesWith(hostAndMetric());
    }

    private int dataEntriesCount() {
        return dataEntriesCount;
    }

    private void calculateActualResult(PerfMonEntries entries) {
        DescriptiveStatistics ds = new DescriptiveStatistics();
        entries.asStream()
                .map(e -> Double.parseDouble(e[VALUE.getColumn()]))
                .forEach(ds::addValue);
        actualResult = (int) ds.getMean();
    }

    protected abstract void calculateTestResult();
}
