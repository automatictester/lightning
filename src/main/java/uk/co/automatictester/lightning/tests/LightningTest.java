package uk.co.automatictester.lightning.tests;

import org.apache.commons.lang3.NotImplementedException;
import uk.co.automatictester.lightning.data.JMeterTransactions;
import uk.co.automatictester.lightning.enums.TestResult;

import java.util.ArrayList;
import java.util.List;

public abstract class LightningTest {

    protected final String name;
    protected final String description;
    protected final String type;
    protected String expectedResult;
    protected String actualResult;
    protected TestResult result;
    protected int transactionCount;
    protected boolean regexp;

    protected LightningTest(String name, String type, String description) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.expectedResult = "";
        this.actualResult = "";
        this.result = null;
        this.regexp = false;

    }

    public abstract void printTestExecutionReport();

    public abstract void execute(ArrayList<ArrayList<String>> dataEntries);

    protected String getDescriptionForReport() {
        return (!getDescription().isEmpty()) ? (String.format("Test description:     %s%n", getDescription())) : "";}

    public JMeterTransactions filterTransactions(JMeterTransactions originalJMeterTransactions) {
        if (getName() != null) {
            if (regexp) {
                return originalJMeterTransactions.includeRegexpLabels(getName());
            }
            else {
                return originalJMeterTransactions.excludeLabelsOtherThan(getName());
            }
        } else {
            return originalJMeterTransactions;
        }
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

    public String getExpectedResult() {
        return expectedResult;
    }

    public String getActualResult() {
        return actualResult;
    }

    public TestResult getResult() {
        return result;
    }

    public void setRegexp(boolean regexp) {
        this.regexp = regexp;
    }

    public List<Integer> getLongestTransactions() {
        throw new NotImplementedException("Method not implemented for LightningTest which is not RespTimeBasedTest");
    }
}
