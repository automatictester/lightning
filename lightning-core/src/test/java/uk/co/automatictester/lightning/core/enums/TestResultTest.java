package uk.co.automatictester.lightning.core.enums;

import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class TestResultTest {

    @Test
    public void testPassToString() {
        assertThat(TestResult.PASS.toString(), is(equalTo("Pass")));
    }
}
