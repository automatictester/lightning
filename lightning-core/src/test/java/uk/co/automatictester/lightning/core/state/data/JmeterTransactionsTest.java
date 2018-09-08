package uk.co.automatictester.lightning.core.state.data;

import org.testng.annotations.Test;
import uk.co.automatictester.lightning.core.exceptions.CSVFileIOException;
import uk.co.automatictester.lightning.core.exceptions.CSVFileNonexistentLabelException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static uk.co.automatictester.lightning.shared.LegacyTestData.*;

public class JmeterTransactionsTest {

    @Test
    public void verifyReadMethod() {
        JmeterTransactions jmeterTransactions = JmeterTransactions.fromFile(CSV_2_TRANSACTIONS);
        assertThat(jmeterTransactions.entries(), hasItem(LOGIN_3514_SUCCESS));
        assertThat(jmeterTransactions.entries(), hasItem(SEARCH_11221_SUCCESS));
    }

    @Test(expectedExceptions = CSVFileIOException.class)
    public void verifyReadMethodIOException() {
        JmeterTransactions.fromFile(CSV_NONEXISTENT);
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void verifyReadMethodNoTransactionsException() {
        JmeterTransactions.fromFile(CSV_0_TRANSACTIONS);
    }

    @Test
    public void testExcludeLabelsOtherThanMethod() {
        List<String[]> testData = new ArrayList<>();
        testData.add(new String[]{"Login", "1200", "true"});
        testData.add(new String[]{"Login", "1000", "true"});
        testData.add(new String[]{"Search", "800", "true"});
        JmeterTransactions jmeterTransactions = JmeterTransactions.fromList(testData);

        assertThat(jmeterTransactions.transactionsWith("Login").size(), is(2));
    }

    @Test(expectedExceptions = CSVFileNonexistentLabelException.class)
    public void testExcludeLabelsOtherThanMethodException() {
        List<String[]> testData = new ArrayList<>();
        testData.add(new String[]{"Login", "1200", "true"});
        JmeterTransactions jmeterTransactions = JmeterTransactions.fromList(testData);

        jmeterTransactions.transactionsWith("nonexistent");
    }

    @Test
    public void testExcludeLabelsNotMatchingMethod() {
        List<String[]> testData = new ArrayList<>();
        testData.add(new String[]{"Login", "1200", "true"});
        testData.add(new String[]{"My Login", "1000", "true"});
        testData.add(new String[]{"Logout", "800", "true"});
        testData.add(new String[]{"Logo", "800", "true"});
        testData.add(new String[]{"LogANY", "1100", "true"});
        JmeterTransactions jmeterTransactions = JmeterTransactions.fromList(testData);

        assertThat(jmeterTransactions.transactionsMatching("Log.{2,3}").size(), is(3));
    }

    @Test(expectedExceptions = CSVFileNonexistentLabelException.class)
    public void testExcludeLabelsNotMatchingMethodException() {
        List<String[]> testData = new ArrayList<>();
        testData.add(new String[]{"Login", "1200", "true"});
        JmeterTransactions jmeterTransactions = JmeterTransactions.fromList(testData);

        jmeterTransactions.transactionsMatching("nonexistent");
    }

    @Test
    public void testGetFailCount() {
        List<String[]> testData = new ArrayList<>();
        testData.add(new String[]{"Login", "1200", "true"});
        testData.add(new String[]{"Login", "1000", "true"});
        testData.add(new String[]{"Search", "800", "true"});
        testData.add(new String[]{"Login", "1200", "false"});
        testData.add(new String[]{"Search", "800", "false"});
        JmeterTransactions jmeterTransactions = JmeterTransactions.fromList(testData);

        assertThat(jmeterTransactions.failCount(), is(equalTo(2)));
    }

    @Test
    public void testTransactionCount() {
        List<String[]> testData = new ArrayList<>();
        testData.add(new String[]{"Login", "1200", "true"});
        testData.add(new String[]{"Login", "1000", "true"});
        testData.add(new String[]{"Search", "800", "true"});
        testData.add(new String[]{"Login", "1200", "false"});
        testData.add(new String[]{"Search", "800", "false"});
        JmeterTransactions jmeterTransactions = JmeterTransactions.fromList(testData);

        assertThat(jmeterTransactions.size(), is(equalTo(5)));
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
        JmeterTransactions jmeterTransactions = JmeterTransactions.fromList(testData);

        List<Integer> expectedResult = new ArrayList<>(Arrays.asList(721, 650, 500, 421, 345));
        assertThat(jmeterTransactions.longestTransactions(), contains(expectedResult.toArray()));
    }

    @Test
    public void testGetLongestTransactions_five() {
        List<String[]> testData = new ArrayList<>();
        testData.add(new String[]{"Login", "421", "true", "1434291240000"});
        testData.add(new String[]{"Login", "500", "true", "1434291245000"});
        testData.add(new String[]{"Login", "345", "true", "1434291246000"});
        testData.add(new String[]{"Login", "650", "true", "1434291245000"});
        testData.add(new String[]{"Login", "721", "true", "1434291246000"});
        JmeterTransactions jmeterTransactions = JmeterTransactions.fromList(testData);

        List<Integer> expectedResult = new ArrayList<>(Arrays.asList(721, 650, 500, 421, 345));
        assertThat(jmeterTransactions.longestTransactions(), contains(expectedResult.toArray()));
    }

    @Test
    public void testGetLongestTransactions_lessThanFive() {
        List<String[]> testData = new ArrayList<>();
        testData.add(new String[]{"Login", "345", "true", "1434291246000"});
        testData.add(new String[]{"Login", "650", "true", "1434291245000"});
        testData.add(new String[]{"Login", "721", "true", "1434291246000"});
        JmeterTransactions jmeterTransactions = JmeterTransactions.fromList(testData);

        List<Integer> expectedResult = new ArrayList<>(Arrays.asList(721, 650, 345));
        assertThat(jmeterTransactions.longestTransactions(), contains(expectedResult.toArray()));
    }

    @Test
    public void testPrintJMeterReport() {
        List<String[]> testData = new ArrayList<>();
        testData.add(new String[]{"Login", "421", "true", "1434291240000"});
        testData.add(new String[]{"Login", "500", "false", "1434291245000"});
        testData.add(new String[]{"Login", "345", "true", "1434291246000"});
        testData.add(new String[]{"Login", "650", "true", "1434291245000"});
        testData.add(new String[]{"Login", "721", "false", "1434291246000"});
        JmeterTransactions jmeterTransactions = JmeterTransactions.fromList(testData);

        String output = jmeterTransactions.getJmeterReport();
        assertThat(output, containsString("Transactions executed: 5, failed: 2"));
    }
}