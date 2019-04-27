package uk.co.automatictester.lightning.core.state.data;

import org.testng.annotations.Test;
import uk.co.automatictester.lightning.core.exceptions.CSVFileNonexistentLabelException;
import uk.co.automatictester.lightning.core.readers.JmeterBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

public class JmeterTransactionsTest {

    @Test
    public void testExcludeLabelsOtherThanMethod() {
        List<JmeterBean> testData = new ArrayList<>();
        testData.add(new JmeterBean("Login", "1200", "true"));
        testData.add(new JmeterBean("Login", "1000", "true"));
        testData.add(new JmeterBean("Search", "800", "true"));
        JmeterTransactions jmeterTransactions = JmeterTransactions.fromList(testData);

        assertThat(jmeterTransactions.transactionsWith("Login").size(), is(2));
    }

    @Test(expectedExceptions = CSVFileNonexistentLabelException.class)
    public void testExcludeLabelsOtherThanMethodException() {
        List<JmeterBean> testData = new ArrayList<>();
        testData.add(new JmeterBean("Login", "1200", "true"));
        JmeterTransactions jmeterTransactions = JmeterTransactions.fromList(testData);

        jmeterTransactions.transactionsWith("nonexistent");
    }

    @Test
    public void testExcludeLabelsNotMatchingMethod() {
        List<JmeterBean> testData = new ArrayList<>();
        testData.add(new JmeterBean("Login", "1200", "true"));
        testData.add(new JmeterBean("My Login", "1000", "true"));
        testData.add(new JmeterBean("Logout", "800", "true"));
        testData.add(new JmeterBean("Logo", "800", "true"));
        testData.add(new JmeterBean("LogANY", "1100", "true"));
        JmeterTransactions jmeterTransactions = JmeterTransactions.fromList(testData);

        assertThat(jmeterTransactions.transactionsMatching("Log.{2,3}").size(), is(3));
    }

    @Test(expectedExceptions = CSVFileNonexistentLabelException.class)
    public void testExcludeLabelsNotMatchingMethodException() {
        List<JmeterBean> testData = new ArrayList<>();
        testData.add(new JmeterBean("Login", "1200", "true"));
        JmeterTransactions jmeterTransactions = JmeterTransactions.fromList(testData);

        jmeterTransactions.transactionsMatching("nonexistent");
    }

    @Test
    public void testGetFailCount() {
        List<JmeterBean> testData = new ArrayList<>();
        testData.add(new JmeterBean("Login", "1200", "true"));
        testData.add(new JmeterBean("Login", "1000", "true"));
        testData.add(new JmeterBean("Search", "800", "true"));
        testData.add(new JmeterBean("Login", "1200", "false"));
        testData.add(new JmeterBean("Search", "800", "false"));
        JmeterTransactions jmeterTransactions = JmeterTransactions.fromList(testData);

        assertThat(jmeterTransactions.failCount(), is(equalTo(2)));
    }

    @Test
    public void testTransactionCount() {
        List<JmeterBean> testData = new ArrayList<>();
        testData.add(new JmeterBean("Login", "1200", "true"));
        testData.add(new JmeterBean("Login", "1000", "true"));
        testData.add(new JmeterBean("Search", "800", "true"));
        testData.add(new JmeterBean("Login", "1200", "false"));
        testData.add(new JmeterBean("Search", "800", "false"));
        JmeterTransactions jmeterTransactions = JmeterTransactions.fromList(testData);

        assertThat(jmeterTransactions.size(), is(equalTo(5)));
    }

    @Test
    public void testGetLongestTransactions_moreThanFive() {
        List<JmeterBean> testData = new ArrayList<>();
        testData.add(new JmeterBean("Login", "421", "true", "1434291240000"));
        testData.add(new JmeterBean("Login", "500", "true", "1434291245000"));
        testData.add(new JmeterBean("Login", "345", "true", "1434291246000"));
        testData.add(new JmeterBean("Login", "2", "true", "1434291245000"));
        testData.add(new JmeterBean("Login", "334", "true", "1434291246000"));
        testData.add(new JmeterBean("Login", "650", "true", "1434291245000"));
        testData.add(new JmeterBean("Login", "721", "true", "1434291246000"));
        JmeterTransactions jmeterTransactions = JmeterTransactions.fromList(testData);

        List<Integer> expectedResult = new ArrayList<>(Arrays.asList(721, 650, 500, 421, 345));
        assertThat(jmeterTransactions.longestTransactions(), contains(expectedResult.toArray()));
    }

    @Test
    public void testGetLongestTransactions_five() {
        List<JmeterBean> testData = new ArrayList<>();
        testData.add(new JmeterBean("Login", "421", "true", "1434291240000"));
        testData.add(new JmeterBean("Login", "500", "true", "1434291245000"));
        testData.add(new JmeterBean("Login", "345", "true", "1434291246000"));
        testData.add(new JmeterBean("Login", "650", "true", "1434291245000"));
        testData.add(new JmeterBean("Login", "721", "true", "1434291246000"));
        JmeterTransactions jmeterTransactions = JmeterTransactions.fromList(testData);

        List<Integer> expectedResult = new ArrayList<>(Arrays.asList(721, 650, 500, 421, 345));
        assertThat(jmeterTransactions.longestTransactions(), contains(expectedResult.toArray()));
    }

    @Test
    public void testGetLongestTransactions_lessThanFive() {
        List<JmeterBean> testData = new ArrayList<>();
        testData.add(new JmeterBean("Login", "345", "true", "1434291246000"));
        testData.add(new JmeterBean("Login", "650", "true", "1434291245000"));
        testData.add(new JmeterBean("Login", "721", "true", "1434291246000"));
        JmeterTransactions jmeterTransactions = JmeterTransactions.fromList(testData);

        List<Integer> expectedResult = new ArrayList<>(Arrays.asList(721, 650, 345));
        assertThat(jmeterTransactions.longestTransactions(), contains(expectedResult.toArray()));
    }
}
