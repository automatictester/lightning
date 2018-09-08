package uk.co.automatictester.lightning.core.config;

import uk.co.automatictester.lightning.core.state.tests.TestSet;

public interface ConfigReader {

    TestSet readTests(String xmlFile);
}
