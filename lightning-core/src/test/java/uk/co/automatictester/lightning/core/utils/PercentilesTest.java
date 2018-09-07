package uk.co.automatictester.lightning.core.utils;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PercentilesTest {

    @DataProvider(name = "positiveTestData")
    private Object[][] positiveTestData() {
        return new Integer[][]{
                {1},
                {100}
        };
    }

    @DataProvider(name = "negativeTestData")
    private Object[][] negativeTestData() {
        return new Integer[][]{
                {-1},
                {0},
                {101}
        };
    }

    @Test(dataProvider = "positiveTestData")
    public void testIsPercentileTrue(int integer) {
        assertThat(Percentiles.isPercentile(integer), is(true));
    }

    @Test(dataProvider = "negativeTestData")
    public void testIsPercentileFalse(int integer) {
        assertThat(Percentiles.isPercentile(integer), is(false));
    }
}