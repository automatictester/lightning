package uk.co.automatictester.lightning.reporters;

import org.testng.annotations.Test;
import org.w3c.dom.Element;
import uk.co.automatictester.lightning.TestSet;
import uk.co.automatictester.lightning.enums.TestResult;
import uk.co.automatictester.lightning.tests.LightningTest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JUnitReporterTest {

    @Test
    public void testGetTestsuite() {
        TestSet testSet = mock(TestSet.class);
        when(testSet.getTestCount()).thenReturn(3);
        when(testSet.getErrorCount()).thenReturn(1);
        when(testSet.getFailCount()).thenReturn(1);

        JUnitReporter jUnitReporter = new JUnitReporter();
        jUnitReporter.setTestSet(testSet);
        Element testsuite = jUnitReporter.getTestsuite();
        assertThat(testsuite.getTagName(), equalTo("testsuite"));
        assertThat(testsuite.getAttribute("tests"), equalTo("3"));
        assertThat(testsuite.getAttribute("errors"), equalTo("1"));
        assertThat(testsuite.getAttribute("failures"), equalTo("1"));
        assertThat(testsuite.getAttribute("time"), equalTo("0"));
        assertThat(testsuite.getAttribute("name"), equalTo("Lightning"));
    }

    @Test
    public void testGetPassedTestcase() {
        LightningTest test = mock(LightningTest.class);
        when(test.getResult()).thenReturn(TestResult.PASS);
        when(test.getName()).thenReturn("some name");

        Element testcase = new JUnitReporter().getTestcase(test);
        assertThat(testcase.getTagName(), equalTo("testcase"));
        assertThat(testcase.getAttribute("time"), equalTo("0"));
        assertThat(testcase.getAttribute("name"), equalTo("some name"));
    }

    @Test
    public void testGetFailedTestcase() {
        LightningTest test = mock(LightningTest.class);
        when(test.getResult()).thenReturn(TestResult.FAIL);
        when(test.getName()).thenReturn("some name");
        when(test.getTestExecutionReport()).thenReturn("some content");
        when(test.getActualResultDescription()).thenReturn("some message");
        when(test.getType()).thenReturn("some type");

        Element testcase = new JUnitReporter().getTestcase(test);
        assertThat(testcase.getTagName(), equalTo("testcase"));
        assertThat(testcase.getAttribute("time"), equalTo("0"));
        assertThat(testcase.getAttribute("name"), equalTo("some name"));
        assertThat(testcase.getTextContent(), equalTo("some content"));
        assertThat(testcase.getElementsByTagName("failure").item(0).getAttributes().item(0).toString(), equalTo("message=\"some message\""));
        assertThat(testcase.getElementsByTagName("failure").item(0).getAttributes().item(1).toString(), equalTo("type=\"some type\""));
    }

    @Test
    public void testGetErrorTestcase() {
        LightningTest test = mock(LightningTest.class);
        when(test.getResult()).thenReturn(TestResult.ERROR);
        when(test.getName()).thenReturn("some name");
        when(test.getTestExecutionReport()).thenReturn("some content");
        when(test.getActualResultDescription()).thenReturn("some message");
        when(test.getType()).thenReturn("some type");

        Element testcase = new JUnitReporter().getTestcase(test);
        assertThat(testcase.getTagName(), equalTo("testcase"));
        assertThat(testcase.getAttribute("time"), equalTo("0"));
        assertThat(testcase.getAttribute("name"), equalTo("some name"));
        assertThat(testcase.getTextContent(), equalTo("some content"));
        assertThat(testcase.getElementsByTagName("error").item(0).getAttributes().item(0).toString(), equalTo("message=\"some message\""));
        assertThat(testcase.getElementsByTagName("error").item(0).getAttributes().item(1).toString(), equalTo("type=\"some type\""));
    }
}