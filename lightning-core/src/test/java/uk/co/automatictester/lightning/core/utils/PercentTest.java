package uk.co.automatictester.lightning.core.utils;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

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
        assertThat(p.value(), is(equalTo((integer))));
        assertThat(p.toString(), is(equalTo((String.valueOf(integer)))));
    }

    @Test(dataProvider = "negativeTestData", expectedExceptions = IllegalArgumentException.class)
    public void testIsPercentileFalse(int integer) {
        Percent.from(integer);
    }
}