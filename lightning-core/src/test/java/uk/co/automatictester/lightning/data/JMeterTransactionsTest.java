package uk.co.automatictester.lightning.data;

import org.testng.annotations.Test;
import uk.co.automatictester.lightning.exceptions.CSVFileIOException;
import uk.co.automatictester.lightning.exceptions.CSVFileNoTransactionsException;
import uk.co.automatictester.lightning.exceptions.CSVFileNonexistentLabelException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.number.IsCloseTo.closeTo;
import static uk.co.automatictester.lightning.shared.TestData.*;

public class JMeterTransactionsTest {

    @Test
    public void verifyReadMethod() {
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromFile(CSV_2_TRANSACTIONS);
        assertThat(jmeterTransactions, hasItem(LOGIN_3514_SUCCESS));
        assertThat(jmeterTransactions, hasItem(SEARCH_11221_SUCCESS));
    }

    @Test(expectedExceptions = CSVFileIOException.class)
    public void verifyReadMethodIOException() {
        JMeterTransactions.fromFile(CSV_NONEXISTENT);
    }

    @Test(expectedExceptions = CSVFileNoTransactionsException.class)
    public void verifyReadMethodNoTransactionsException() {
        JMeterTransactions.fromFile(CSV_0_TRANSACTIONS);
    }

    @Test
    public void testExcludeLabelsOtherThanMethod() {
        List<String[]> testData = new ArrayList<>();
        testData.add(new String[]{"Login", "1200", "true"});
        testData.add(new String[]{"Login", "1000", "true"});
        testData.add(new String[]{"Search", "800", "true"});
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        assertThat(jmeterTransactions.getTransactionsWith("Login").size(), is(2));
    }

    @Test(expectedExceptions = CSVFileNonexistentLabelException.class)
    public void testExcludeLabelsOtherThanMethodException() {
        List<String[]> testData = new ArrayList<>();
        testData.add(new String[]{"Login", "1200", "true"});
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        jmeterTransactions.getTransactionsWith("nonexistent");
    }

    @Test
    public void testExcludeLabelsNotMatchingMethod() {
        List<String[]> testData = new ArrayList<>();
        testData.add(new String[]{"Login", "1200", "true"});
        testData.add(new String[]{"My Login", "1000", "true"});
        testData.add(new String[]{"Logout", "800", "true"});
        testData.add(new String[]{"Logo", "800", "true"});
        testData.add(new String[]{"LogANY", "1100", "true"});
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        assertThat(jmeterTransactions.getTransactionsMatching("Log.{2,3}").size(), is(3));
    }

    @Test(expectedExceptions = CSVFileNonexistentLabelException.class)
    public void testExcludeLabelsNotMatchingMethodException() {
        List<String[]> testData = new ArrayList<>();
        testData.add(new String[]{"Login", "1200", "true"});
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        jmeterTransactions.getTransactionsMatching("nonexistent");
    }

    @Test
    public void testGetFailCount() {
        List<String[]> testData = new ArrayList<>();
        testData.add(new String[]{"Login", "1200", "true"});
        testData.add(new String[]{"Login", "1000", "true"});
        testData.add(new String[]{"Search", "800", "true"});
        testData.add(new String[]{"Login", "1200", "false"});
        testData.add(new String[]{"Search", "800", "false"});
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        assertThat(jmeterTransactions.getFailCount(), is(equalTo(2)));
    }

    @Test
    public void testTransactionCount() {
        List<String[]> testData = new ArrayList<>();
        testData.add(new String[]{"Login", "1200", "true"});
        testData.add(new String[]{"Login", "1000", "true"});
        testData.add(new String[]{"Search", "800", "true"});
        testData.add(new String[]{"Login", "1200", "false"});
        testData.add(new String[]{"Search", "800", "false"});
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        assertThat(jmeterTransactions.size(), is(equalTo(5)));
    }

    @Test
    public void testGetThroughputForOrderedTransactions() {
        List<String[]> testData = new ArrayList<>();
        testData.add(new String[]{"Login", "123", "true", "1434291243000"});
        testData.add(new String[]{"Login", "213", "true", "1434291244000"});
        testData.add(new String[]{"Login", "222", "true", "1434291245000"});
        testData.add(new String[]{"Login", "333", "true", "1434291246000"});
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        assertThat(jmeterTransactions.getThroughput(), is(closeTo(1.33, 0.01)));
    }

    @Test
    public void testGetThroughputForUnorderedTransactions() {
        List<String[]> testData = new ArrayList<>();
        testData.add(new String[]{"Login", "560", "true", "1434291246000"});
        testData.add(new String[]{"Login", "650", "true", "1434291244000"});
        testData.add(new String[]{"Login", "700", "true", "1434291245000"});
        testData.add(new String[]{"Login", "400", "true", "1434291243000"});
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        assertThat(jmeterTransactions.getThroughput(), is(closeTo(1.33, 0.01)));
    }

    @Test
    public void testGetThroughputForOneTransactionPerMillisecond() {
        List<String[]> testData = new ArrayList<>();
        testData.add(new String[]{"Login", "111", "true", "1434291240001"});
        testData.add(new String[]{"Login", "157", "true", "1434291240002"});
        testData.add(new String[]{"Login", "243", "true", "1434291240004"});
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        assertThat(jmeterTransactions.getThroughput(), is(closeTo(1000, 0.01)));
    }

    @Test
    public void testGetThroughputForMoreThanOneTransactionPerMillisecond() {
        List<String[]> testData = new ArrayList<>();
        testData.add(new String[]{"Login", "123", "true", "1434291240001"});
        testData.add(new String[]{"Login", "142", "true", "1434291240002"});
        testData.add(new String[]{"Login", "165", "true", "1434291240003"});
        testData.add(new String[]{"Login", "109", "true", "1434291240004"});
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        assertThat(jmeterTransactions.getThroughput(), is(closeTo(1333.33, 0.01)));
    }

    @Test
    public void testGetThroughputForLessThanOneTransactionPerSecond() {
        List<String[]> testData = new ArrayList<>();
        testData.add(new String[]{"Login", "100", "true", "1434291240000"});
        testData.add(new String[]{"Login", "124", "true", "1434291245000"});
        testData.add(new String[]{"Login", "250", "true", "1434291246000"});
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        assertThat(jmeterTransactions.getThroughput(), is(closeTo(0.5, 0.01)));
    }

    @Test
    public void testGetLongestTransactions_moreThanFive() {
        List<String[]> testData = new ArrayList<>();
        testData.add(new String[]{"Login", "421", "true", "1434291240000"});
        testData.add(new String[]{"Login", "500", "true", "1434291245000"});
        testData.add(new String[]{"Login", "345", "true", "1434291246000"});
        testData.add(new String[]{"Login", "2", "true", "1434291245000"});
        testData.add(new String[]{"Login", "334", "true", "1434291246000"});
        testData.add(new String[]{"Login", "650", "true", "1434291245000"});
        testData.add(new String[]{"Login", "721", "true", "1434291246000"});
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        List<Integer> expectedResult = new ArrayList<>(Arrays.asList(721, 650, 500, 421, 345));
        assertThat(jmeterTransactions.getLongestTransactions(), contains(expectedResult.toArray()));
    }

    @Test
    public void testGetLongestTransactions_five() {
        List<String[]> testData = new ArrayList<>();
        testData.add(new String[]{"Login", "421", "true", "1434291240000"});
        testData.add(new String[]{"Login", "500", "true", "1434291245000"});
        testData.add(new String[]{"Login", "345", "true", "1434291246000"});
        testData.add(new String[]{"Login", "650", "true", "1434291245000"});
        testData.add(new String[]{"Login", "721", "true", "1434291246000"});
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        List<Integer> expectedResult = new ArrayList<>(Arrays.asList(721, 650, 500, 421, 345));
        assertThat(jmeterTransactions.getLongestTransactions(), contains(expectedResult.toArray()));
    }

    @Test
    public void testGetLongestTransactions_lessThanFive() {
        List<String[]> testData = new ArrayList<>();
        testData.add(new String[]{"Login", "345", "true", "1434291246000"});
        testData.add(new String[]{"Login", "650", "true", "1434291245000"});
        testData.add(new String[]{"Login", "721", "true", "1434291246000"});
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        List<Integer> expectedResult = new ArrayList<>(Arrays.asList(721, 650, 345));
        assertThat(jmeterTransactions.getLongestTransactions(), contains(expectedResult.toArray()));
    }
}