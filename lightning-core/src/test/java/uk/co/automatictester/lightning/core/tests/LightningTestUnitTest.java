package uk.co.automatictester.lightning.core.tests;

import org.mockito.Mockito;
import org.testng.annotations.Test;
import uk.co.automatictester.lightning.core.data.JMeterTransactions;
import uk.co.automatictester.lightning.core.tests.base.ClientSideTest;
import uk.co.automatictester.lightning.shared.LegacyTestData;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.when;

public class LightningTestUnitTest {

    @Test
    public void testFilterTransactionsSome() {
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.LOGIN_1000_SUCCESS);
        testData.add(LegacyTestData.SEARCH_800_SUCCESS);
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        ClientSideTest test = Mockito.mock(ClientSideTest.class, Mockito.CALLS_REAL_METHODS);
        when(test.getTransactionName()).thenReturn("Search");

        JMeterTransactions filteredTransactions = test.filterTransactions(jmeterTransactions);
        assertThat(filteredTransactions.size(), is(equalTo((1))));
    }

    @Test
    public void testFilterTransactionsAll() {
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.LOGIN_1000_SUCCESS);
        testData.add(LegacyTestData.SEARCH_800_SUCCESS);
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        ClientSideTest test = Mockito.mock(ClientSideTest.class, Mockito.CALLS_REAL_METHODS);
        when(test.getTransactionName()).thenReturn(null);

        JMeterTransactions filteredTransactions = test.filterTransactions(jmeterTransactions);
        assertThat(filteredTransactions.size(), is(equalTo((2))));
    }
}