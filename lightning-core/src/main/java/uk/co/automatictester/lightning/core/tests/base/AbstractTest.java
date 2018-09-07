package uk.co.automatictester.lightning.core.tests.base;

import uk.co.automatictester.lightning.core.enums.TestResult;

import static org.apache.commons.lang3.StringUtils.isBlank;

public abstract class AbstractTest {

    protected final String type;
    protected final String name;
    protected String description;
    protected String expectedResultDescription;
    protected String actualResultDescription;
    protected TestResult result;
    protected int actualResult;

    protected AbstractTest(String testType, String testName) {
        this.type = testType;
        this.name = testName;
        this.expectedResultDescription = "";
        this.actualResultDescription = "";
        this.result = null;
        this.actualResult = 0;
    }

    public String name() {
        return name;
    }

    public String type() {
        return type;
    }

    public String description() {
        return description;
    }

    public String expectedResultDescription() {
        return expectedResultDescription;
    }

    public String actualResultDescription() {
        return actualResultDescription;
    }

    public TestResult result() {
        return result;
    }

    public int actualResult() {
        return actualResult;
    }

    String descriptionForReport() {
        String message = String.format("Test description:     %s%n", description());
        return isBlank(description()) ? "" : message;
    }

    String resultForReport() {
        return result.toString();
    }

    public abstract String getTestExecutionReport();

    public abstract void execute();
}
