package uk.co.automatictester.lightning.core.reporters.junit;

import org.testng.annotations.Test;
import org.w3c.dom.Element;
import uk.co.automatictester.lightning.core.enums.TestResult;
import uk.co.automatictester.lightning.core.state.tests.TestSet;
import uk.co.automatictester.lightning.core.tests.base.AbstractTest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JunitReporterTest {

    @Test
    public void testGetTestsuite() {
        TestSet testSet = mock(TestSet.class);
        when(testSet.size()).thenReturn(3);
        when(testSet.errorCount()).thenReturn(1);
        when(testSet.failCount()).thenReturn(1);

        Element testsuite = new JunitReporter().getTestsuite(testSet);
        assertThat(testsuite.getTagName(), equalTo("testsuite"));
        assertThat(testsuite.getAttribute("tests"), equalTo("3"));
        assertThat(testsuite.getAttribute("errors"), equalTo("1"));
        assertThat(testsuite.getAttribute("failures"), equalTo("1"));
        assertThat(testsuite.getAttribute("time"), equalTo("0"));
        assertThat(testsuite.getAttribute("name"), equalTo("Lightning"));
    }

    @Test
    public void testGetPassedTestcase() {
        AbstractTest test = mock(AbstractTest.class);
        when(test.result()).thenReturn(TestResult.PASS);
        when(test.name()).thenReturn("some name");

        Element testcase = new JunitReporter().getTestcase(test);
        assertThat(testcase.getTagName(), equalTo("testcase"));
        assertThat(testcase.getAttribute("time"), equalTo("0"));
        assertThat(testcase.getAttribute("name"), equalTo("some name"));
    }

    @Test
    public void testGetFailedTestcase() {
        AbstractTest test = mock(AbstractTest.class);
        when(test.result()).thenReturn(TestResult.FAIL);
        when(test.name()).thenReturn("some name");
        when(test.getTestExecutionReport()).thenReturn("some content");
        when(test.actualResultDescription()).thenReturn("some message");
        when(test.type()).thenReturn("some type");

        Element testcase = new JunitReporter().getTestcase(test);
        assertThat(testcase.getTagName(), equalTo("testcase"));
        assertThat(testcase.getAttribute("time"), equalTo("0"));
        assertThat(testcase.getAttribute("name"), equalTo("some name"));
        assertThat(testcase.getTextContent(), equalTo("some content"));
        assertThat(testcase.getElementsByTagName("failure").item(0).getAttributes().item(0).toString(), equalTo("message=\"some message\""));
        assertThat(testcase.getElementsByTagName("failure").item(0).getAttributes().item(1).toString(), equalTo("type=\"some type\""));
    }

    @Test
    public void testGetErrorTestcase() {
        AbstractTest test = mock(AbstractTest.class);
        when(test.result()).thenReturn(TestResult.ERROR);
        when(test.name()).thenReturn("some name");
        when(test.getTestExecutionReport()).thenReturn("some content");
        when(test.actualResultDescription()).thenReturn("some message");
        when(test.type()).thenReturn("some type");

        Element testcase = new JunitReporter().getTestcase(test);
        assertThat(testcase.getTagName(), equalTo("testcase"));
        assertThat(testcase.getAttribute("time"), equalTo("0"));
        assertThat(testcase.getAttribute("name"), equalTo("some name"));
        assertThat(testcase.getTextContent(), equalTo("some content"));
        assertThat(testcase.getElementsByTagName("error").item(0).getAttributes().item(0).toString(), equalTo("message=\"some message\""));
        assertThat(testcase.getElementsByTagName("error").item(0).getAttributes().item(1).toString(), equalTo("type=\"some type\""));
    }
}