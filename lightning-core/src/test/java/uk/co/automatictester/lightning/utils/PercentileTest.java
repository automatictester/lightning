package uk.co.automatictester.lightning.utils;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PercentileTest {

    @DataProvider(name = "positiveTestData")
    private Integer[][] positiveTestData() {
        return new Integer[][]{
                {1},
                {100}
        };
    }

    @DataProvider(name = "negativeTestData")
    private Integer[][] negativeTestData() {
        return new Integer[][]{
                {-1},
                {0},
                {101}
        };
    }

    @Test(dataProvider = "positiveTestData")
    public void testIsPercentileTrue(int integer) {
        Percentile p = new Percentile();
        assertThat(p.isPercentile(integer), is(true));
    }

    @Test(dataProvider = "negativeTestData")
    public void testIsPercentileFalse(int integer) {
        Percentile p = new Percentile();
        assertThat(p.isPercentile(integer), is(false));
    }
}