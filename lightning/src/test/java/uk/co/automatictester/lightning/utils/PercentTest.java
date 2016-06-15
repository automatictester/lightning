package uk.co.automatictester.lightning.utils;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import uk.co.automatictester.lightning.exceptions.PercentException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PercentTest {

    @DataProvider(name = "positiveTestData")
    private Integer[][] positiveTestData() {
        return new Integer[][]{
                {0},
                {100}
        };
    }

    @DataProvider(name = "negativeTestData")
    private Integer[][] negativeTestData() {
        return new Integer[][]{
                {-1},
                {101}
        };
    }

    @Test(dataProvider = "positiveTestData")
    public void testIsPercentileTrue(int integer) {
        Percent p = new Percent(integer);
        assertThat(p.getPercent(), is(equalTo((integer))));
    }

    @Test(dataProvider = "negativeTestData", expectedExceptions = PercentException.class)
    public void testIsPercentileFalse(int integer) {
        new Percent(integer);
    }
}