package uk.co.automatictester.lightning.core.tests;

import uk.co.automatictester.lightning.core.enums.TestResult;

import static org.apache.commons.lang3.StringUtils.isBlank;

public abstract class AbstractLightningTest implements LightningTest {

    protected final String type;
    protected final String name;
    protected String description;
    protected String expectedResultDescription;
    protected String actualResultDescription;
    protected TestResult result;
    protected int actualResult;

    protected AbstractLightningTest(String testType, String testName) {
        this.type = testType;
        this.name = testName;
        this.expectedResultDescription = "";
        this.actualResultDescription = "";
        this.result = null;
        this.actualResult = 0;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String type() {
        return type;
    }

    @Override
    public String actualResultDescription() {
        return actualResultDescription;
    }

    @Override
    public TestResult result() {
        return result;
    }

    @Override
    public int actualResult() {
        return actualResult;
    }

    public String description() {
        return description;
    }

    public String expectedResultDescription() {
        return expectedResultDescription;
    }

    String descriptionForReport() {
        String message = String.format("Test description:     %s%n", description());
        return isBlank(description()) ? "" : message;
    }

    String resultForReport() {
        return result.toString();
    }
}
