package uk.co.automatictester.lightning.core.state.tests;

import org.testng.annotations.Test;
import uk.co.automatictester.lightning.core.tests.RespTimeAvgTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class LightningTestSetTest {

    @Test
    public void testGetInstance() {
        LightningTestSet testsA = LightningTestSet.getInstance();
        testsA.flush();
        RespTimeAvgTest test = new RespTimeAvgTest.Builder("my name", 800).build();
        testsA.add(test);

        LightningTestSet testsB = LightningTestSet.getInstance();

        assertThat(testsA == testsB, is(true));
    }

    @Test
    public void testFlush() {
        LightningTestSet tests = LightningTestSet.getInstance();
        tests.flush();
        RespTimeAvgTest test = new RespTimeAvgTest.Builder("my name", 800).build();
        tests.add(test);
        assertThat(tests.size() == 1, is(true));

        tests.flush();
        assertThat(tests.size() == 0, is(true));
    }
}