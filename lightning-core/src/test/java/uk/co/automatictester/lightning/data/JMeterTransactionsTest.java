package uk.co.automatictester.lightning.data;

import org.testng.annotations.Test;
import uk.co.deliverymind.lightning.exceptions.CSVFileNonexistentLabelException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.number.IsCloseTo.closeTo;

public class JMeterTransactionsTest {

    @Test
    public void testExcludeLabelsOtherThanMethod() {
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(new String[] {"Login", "1200", "true"});
        jmeterTransactions.add(new String[] {"Login", "1000", "true"});
        jmeterTransactions.add(new String[] {"Search", "800", "true"});

        assertThat(jmeterTransactions.excludeLabelsOtherThan("Login").size(), is(2));
    }

    @Test(expectedExceptions = CSVFileNonexistentLabelException.class)
    public void testExcludeLabelsOtherThanMethodException() {
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(new String[] {"Login", "1200", "true"});

        jmeterTransactions.excludeLabelsOtherThan("nonexistent");
    }

    @Test
    public void testExcludeLabelsNotMatchingMethod() {
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(new String[] {"Login", "1200", "true"});
        jmeterTransactions.add(new String[] {"My Login", "1000", "true"});
        jmeterTransactions.add(new String[] {"Logout", "800", "true"});
        jmeterTransactions.add(new String[] {"Logo", "800", "true"});
        jmeterTransactions.add(new String[] {"LogANY", "1100", "true"});

        assertThat(jmeterTransactions.excludeLabelsNotMatching("Log.{2,3}").size(), is(3));
    }

    @Test(expectedExceptions = CSVFileNonexistentLabelException.class)
    public void testExcludeLabelsNotMatchingMethodException() {
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(new String[] {"Login", "1200", "true"});

        jmeterTransactions.excludeLabelsNotMatching("nonexistent");
    }

    @Test
    public void testGetFailCount() {
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(new String[] {"Login", "1200", "true"});
        jmeterTransactions.add(new String[] {"Login", "1000", "true"});
        jmeterTransactions.add(new String[] {"Search", "800", "true"});
        jmeterTransactions.add(new String[] {"Login", "1200", "false"});
        jmeterTransactions.add(new String[] {"Search", "800", "false"});

        assertThat(jmeterTransactions.getFailCount(), is(equalTo(2)));
    }

    @Test
    public void testTransactionCount() {
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(new String[] {"Login", "1200", "true"});
        jmeterTransactions.add(new String[] {"Login", "1000", "true"});
        jmeterTransactions.add(new String[] {"Search", "800", "true"});
        jmeterTransactions.add(new String[] {"Login", "1200", "false"});
        jmeterTransactions.add(new String[] {"Search", "800", "false"});

        assertThat(jmeterTransactions.getTransactionCount(), is(equalTo(5)));
    }

    @Test
    public void testGetThroughputForOrderedTransactions() {
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(new String[] {"Login", "123", "true", "1434291243000"});
        jmeterTransactions.add(new String[] {"Login", "213", "true", "1434291244000"});
        jmeterTransactions.add(new String[] {"Login", "222", "true", "1434291245000"});
        jmeterTransactions.add(new String[] {"Login", "333", "true", "1434291246000"});

        assertThat(jmeterTransactions.getThroughput(), is(closeTo(1.33, 0.01)));
    }

    @Test
    public void testGetThroughputForUnorderedTransactions() {
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(new String[] {"Login", "560", "true", "1434291246000"});
        jmeterTransactions.add(new String[] {"Login", "650", "true", "1434291244000"});
        jmeterTransactions.add(new String[] {"Login", "700", "true", "1434291245000"});
        jmeterTransactions.add(new String[] {"Login", "400", "true", "1434291243000"});

        assertThat(jmeterTransactions.getThroughput(), is(closeTo(1.33, 0.01)));
    }

    @Test
    public void testGetThroughputForOneTransactionPerMillisecond() {
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(new String[] {"Login", "111", "true", "1434291240001"});
        jmeterTransactions.add(new String[] {"Login", "157", "true", "1434291240002"});
        jmeterTransactions.add(new String[] {"Login", "243", "true", "1434291240004"});

        assertThat(jmeterTransactions.getThroughput(), is(closeTo(1000, 0.01)));
    }

    @Test
    public void testGetThroughputForMoreThanOneTransactionPerMillisecond() {
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(new String[] {"Login", "123", "true", "1434291240001"});
        jmeterTransactions.add(new String[] {"Login", "142", "true", "1434291240002"});
        jmeterTransactions.add(new String[] {"Login", "165", "true", "1434291240003"});
        jmeterTransactions.add(new String[] {"Login", "109", "true", "1434291240004"});

        assertThat(jmeterTransactions.getThroughput(), is(closeTo(1333.33, 0.01)));
    }

    @Test
    public void testGetThroughputForLessThanOneTransactionPerSecond() {
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(new String[] {"Login", "100", "true", "1434291240000"});
        jmeterTransactions.add(new String[] {"Login", "124", "true", "1434291245000"});
        jmeterTransactions.add(new String[] {"Login", "250", "true", "1434291246000"});

        assertThat(jmeterTransactions.getThroughput(), is(closeTo(0.5, 0.01)));
    }

    @Test
    public void testGetLongestTransactions_moreThanFive() {
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(new String[] {"Login", "421", "true", "1434291240000"});
        jmeterTransactions.add(new String[] {"Login", "500", "true", "1434291245000"});
        jmeterTransactions.add(new String[] {"Login", "345", "true", "1434291246000"});
        jmeterTransactions.add(new String[] {"Login", "2", "true", "1434291245000"});
        jmeterTransactions.add(new String[] {"Login", "334", "true", "1434291246000"});
        jmeterTransactions.add(new String[] {"Login", "650", "true", "1434291245000"});
        jmeterTransactions.add(new String[] {"Login", "721", "true", "1434291246000"});

        List<Integer> expectedResult = new ArrayList<>(Arrays.asList(721, 650, 500, 421, 345));
        assertThat(jmeterTransactions.getLongestTransactions(), contains(expectedResult.toArray()));
    }

    @Test
    public void testGetLongestTransactions_five() {
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(new String[] {"Login", "421", "true", "1434291240000"});
        jmeterTransactions.add(new String[] {"Login", "500", "true", "1434291245000"});
        jmeterTransactions.add(new String[] {"Login", "345", "true", "1434291246000"});
        jmeterTransactions.add(new String[] {"Login", "650", "true", "1434291245000"});
        jmeterTransactions.add(new String[] {"Login", "721", "true", "1434291246000"});

        List<Integer> expectedResult = new ArrayList<>(Arrays.asList(721, 650, 500, 421, 345));
        assertThat(jmeterTransactions.getLongestTransactions(), contains(expectedResult.toArray()));
    }

    @Test
    public void testGetLongestTransactions_lessThanFive() {
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(new String[] {"Login", "345", "true", "1434291246000"});
        jmeterTransactions.add(new String[] {"Login", "650", "true", "1434291245000"});
        jmeterTransactions.add(new String[] {"Login", "721", "true", "1434291246000"});

        List<Integer> expectedResult = new ArrayList<>(Arrays.asList(721, 650, 345));
        assertThat(jmeterTransactions.getLongestTransactions(), contains(expectedResult.toArray()));
    }
}