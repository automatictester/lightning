package uk.co.automatictester.lightning.data;

import org.testng.annotations.Test;
import uk.co.automatictester.lightning.exceptions.CSVFileNonexistentLabelException;

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
        jmeterTransactions.add(new ArrayList<>(Arrays.asList("Login", "1200", "true")));
        jmeterTransactions.add(new ArrayList<>(Arrays.asList("Login", "1000", "true")));
        jmeterTransactions.add(new ArrayList<>(Arrays.asList("Search", "800", "true")));

        assertThat(jmeterTransactions.excludeLabelsOtherThan("Login").size(), is(2));
    }

    @Test
    public void testIncludeRegexpLabelsMethod() {
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(new ArrayList<>(Arrays.asList("Login", "1200", "true")));
        jmeterTransactions.add(new ArrayList<>(Arrays.asList("Login*test", "1000", "true")));
        jmeterTransactions.add(new ArrayList<>(Arrays.asList("Search", "800", "true")));
        jmeterTransactions.add(new ArrayList<>(Arrays.asList("*.Login", "800", "true")));

        assertThat(jmeterTransactions.includeRegexpLabels("Login").size(), is(3));
    }


    @Test(expectedExceptions = CSVFileNonexistentLabelException.class)
    public void testExcludeLabelsOtherThanMethodException() {
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(new ArrayList<>(Arrays.asList("Login", "1200", "true")));

        jmeterTransactions.excludeLabelsOtherThan("nonexistent");
    }

    @Test
    public void testGetFailCount() {
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(new ArrayList<>(Arrays.asList("Login", "1200", "true")));
        jmeterTransactions.add(new ArrayList<>(Arrays.asList("Login", "1000", "true")));
        jmeterTransactions.add(new ArrayList<>(Arrays.asList("Search", "800", "true")));
        jmeterTransactions.add(new ArrayList<>(Arrays.asList("Login", "1200", "false")));
        jmeterTransactions.add(new ArrayList<>(Arrays.asList("Search", "800", "false")));

        assertThat(jmeterTransactions.getFailCount(), is(equalTo(2)));
    }

    @Test
    public void testTransactionCount() {
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(new ArrayList<>(Arrays.asList("Login", "1200", "true")));
        jmeterTransactions.add(new ArrayList<>(Arrays.asList("Login", "1000", "true")));
        jmeterTransactions.add(new ArrayList<>(Arrays.asList("Search", "800", "true")));
        jmeterTransactions.add(new ArrayList<>(Arrays.asList("Login", "1200", "false")));
        jmeterTransactions.add(new ArrayList<>(Arrays.asList("Search", "800", "false")));

        assertThat(jmeterTransactions.getTransactionCount(), is(equalTo(5)));
    }

    @Test
    public void testGetThroughputForOrderedTransactions() {
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(new ArrayList<>(Arrays.asList("Login", "1000", "true", "1434291243000")));
        jmeterTransactions.add(new ArrayList<>(Arrays.asList("Login", "1000", "true", "1434291244000")));
        jmeterTransactions.add(new ArrayList<>(Arrays.asList("Login", "1000", "true", "1434291245000")));
        jmeterTransactions.add(new ArrayList<>(Arrays.asList("Login", "1000", "true", "1434291246000")));

        assertThat(jmeterTransactions.getThroughput(), is(closeTo(1.33, 0.01)));
    }

    @Test
    public void testGetThroughputForUnorderedTransactions() {
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(new ArrayList<>(Arrays.asList("Login", "1000", "true", "1434291246000")));
        jmeterTransactions.add(new ArrayList<>(Arrays.asList("Login", "1000", "true", "1434291244000")));
        jmeterTransactions.add(new ArrayList<>(Arrays.asList("Login", "1000", "true", "1434291245000")));
        jmeterTransactions.add(new ArrayList<>(Arrays.asList("Login", "1000", "true", "1434291243000")));

        assertThat(jmeterTransactions.getThroughput(), is(closeTo(1.33, 0.01)));
    }

    @Test
    public void testGetThroughputForOneTransactionPerMillisecond() {
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(new ArrayList<>(Arrays.asList("Login", "1000", "true", "1434291240001")));
        jmeterTransactions.add(new ArrayList<>(Arrays.asList("Login", "1000", "true", "1434291240002")));
        jmeterTransactions.add(new ArrayList<>(Arrays.asList("Login", "1000", "true", "1434291240004")));

        assertThat(jmeterTransactions.getThroughput(), is(closeTo(1000, 0.01)));
    }

    @Test
    public void testGetThroughputForMoreThanOneTransactionPerMillisecond() {
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(new ArrayList<>(Arrays.asList("Login", "1000", "true", "1434291240001")));
        jmeterTransactions.add(new ArrayList<>(Arrays.asList("Login", "1000", "true", "1434291240002")));
        jmeterTransactions.add(new ArrayList<>(Arrays.asList("Login", "1000", "true", "1434291240003")));
        jmeterTransactions.add(new ArrayList<>(Arrays.asList("Login", "1000", "true", "1434291240004")));

        assertThat(jmeterTransactions.getThroughput(), is(closeTo(1333.33, 0.01)));
    }

    @Test
    public void testGetThroughputForLessThanOneTransactionPerSecond() {
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(new ArrayList<>(Arrays.asList("Login", "1000", "true", "1434291240000")));
        jmeterTransactions.add(new ArrayList<>(Arrays.asList("Login", "1000", "true", "1434291245000")));
        jmeterTransactions.add(new ArrayList<>(Arrays.asList("Login", "1000", "true", "1434291246000")));

        assertThat(jmeterTransactions.getThroughput(), is(closeTo(0.5, 0.01)));
    }

    @Test
    public void testGetLongestTransactions_moreThanFive() {
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(new ArrayList<>(Arrays.asList("Login", "421", "true", "1434291240000")));
        jmeterTransactions.add(new ArrayList<>(Arrays.asList("Login", "2432", "true", "1434291245000")));
        jmeterTransactions.add(new ArrayList<>(Arrays.asList("Login", "345", "true", "1434291246000")));
        jmeterTransactions.add(new ArrayList<>(Arrays.asList("Login", "2", "true", "1434291245000")));
        jmeterTransactions.add(new ArrayList<>(Arrays.asList("Login", "334", "true", "1434291246000")));
        jmeterTransactions.add(new ArrayList<>(Arrays.asList("Login", "29876", "true", "1434291245000")));
        jmeterTransactions.add(new ArrayList<>(Arrays.asList("Login", "90000", "true", "1434291246000")));

        List<Integer> expectedResult = new ArrayList<>(Arrays.asList(90000, 29876, 2432, 421, 345));
        assertThat(jmeterTransactions.getLongestTransactions(), contains(expectedResult.toArray()));
    }

    @Test
    public void testGetLongestTransactions_five() {
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(new ArrayList<>(Arrays.asList("Login", "421", "true", "1434291240000")));
        jmeterTransactions.add(new ArrayList<>(Arrays.asList("Login", "2432", "true", "1434291245000")));
        jmeterTransactions.add(new ArrayList<>(Arrays.asList("Login", "345", "true", "1434291246000")));
        jmeterTransactions.add(new ArrayList<>(Arrays.asList("Login", "29876", "true", "1434291245000")));
        jmeterTransactions.add(new ArrayList<>(Arrays.asList("Login", "90000", "true", "1434291246000")));

        List<Integer> expectedResult = new ArrayList<>(Arrays.asList(90000, 29876, 2432, 421, 345));
        assertThat(jmeterTransactions.getLongestTransactions(), contains(expectedResult.toArray()));
    }

    @Test
    public void testGetLongestTransactions_lessThanFive() {
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(new ArrayList<>(Arrays.asList("Login", "421", "true", "1434291240000")));
        jmeterTransactions.add(new ArrayList<>(Arrays.asList("Login", "2432", "true", "1434291245000")));
        jmeterTransactions.add(new ArrayList<>(Arrays.asList("Login", "90000", "true", "1434291246000")));

        List<Integer> expectedResult = new ArrayList<>(Arrays.asList(90000, 2432, 421));
        assertThat(jmeterTransactions.getLongestTransactions(), contains(expectedResult.toArray()));
    }
}