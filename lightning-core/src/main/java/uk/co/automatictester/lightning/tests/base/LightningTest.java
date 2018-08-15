package uk.co.automatictester.lightning.tests.base;

import uk.co.automatictester.lightning.enums.TestResult;

import java.util.ArrayList;

import static org.apache.commons.lang3.StringUtils.isBlank;

public abstract class LightningTest {

    protected final String type;
    protected final String name;
    protected String description;
    protected String expectedResultDescription;
    protected String actualResultDescription;
    protected TestResult result;
    protected int actualResult;

    protected LightningTest(String testType, String testName) {
        this.type = testType;
        this.name = testName;
        this.expectedResultDescription = "";
        this.actualResultDescription = "";
        this.result = null;
        this.actualResult = 0;
    }

    public abstract void printTestExecutionReport();

    public abstract String getTestExecutionReport();

    public abstract void execute();

    protected String getDescriptionForReport() {
        String message = String.format("Test description:     %s%n", getDescription());
        return isBlank(getDescription()) ? "" : message;
    }

    protected String getResultForReport() {
        return result.toString();
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public String getExpectedResultDescription() {
        return expectedResultDescription;
    }

    public String getActualResultDescription() {
        return actualResultDescription;
    }

    public TestResult getResult() {
        return result;
    }

    public int getActualResult() {
        return actualResult;
    }
}
