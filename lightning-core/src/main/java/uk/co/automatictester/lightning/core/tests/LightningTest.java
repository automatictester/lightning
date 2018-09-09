package uk.co.automatictester.lightning.core.tests;

import uk.co.automatictester.lightning.core.enums.TestResult;

public interface LightningTest {

    void execute();

    String getTestExecutionReport();

    String name();

    String type();

    String actualResultDescription();

    TestResult result();

    int actualResult();
}
