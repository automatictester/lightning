package uk.co.automatictester.lightning.tests;

import uk.co.automatictester.lightning.enums.TestResult;

import java.util.ArrayList;

public abstract class LightningTest {

    protected final String name;
    protected final String description;
    protected final String type;
    protected String expectedResultDescription;
    protected String actualResultDescription;
    protected TestResult result;
    protected int actualResult;

    protected LightningTest(String name, String type, String description) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.expectedResultDescription = "";
        this.actualResultDescription = "";
        this.result = null;
        this.actualResult = 0;
    }

    public abstract void printTestExecutionReport();

    public abstract String getTestExecutionReport();

    public abstract void execute(ArrayList<String[]> dataEntries);

    protected String getDescriptionForReport() {
        return (!getDescription().isEmpty()) ? String.format("Test description:     %s%n", getDescription()) : "";
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
