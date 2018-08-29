package uk.co.automatictester.lightning.core.utils;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import uk.co.automatictester.lightning.core.exceptions.PercentException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PercentTest {

    @DataProvider(name = "positiveTestData")
    private Object[][] positiveTestData() {
        return new Integer[][]{
                {0},
                {50},
                {100}
        };
    }

    @DataProvider(name = "negativeTestData")
    private Object[][] negativeTestData() {
        return new Integer[][]{
                {-1},
                {101}
        };
    }

    @Test(dataProvider = "positiveTestData")
    public void testIsPercentileTrue(int integer) {
        Percent p = Percent.from(integer);
        assertThat(p.getValue(), is(equalTo((integer))));
    }

    @Test(dataProvider = "negativeTestData", expectedExceptions = PercentException.class)
    public void testIsPercentileFalse(int integer) {
        Percent.from(integer);
    }
}