package uk.co.deliverymind.lightning.readers;

import org.testng.annotations.Test;
import uk.co.deliverymind.lightning.data.JMeterTransactions;
import uk.co.deliverymind.lightning.exceptions.CSVFileIOException;
import uk.co.deliverymind.lightning.exceptions.CSVFileNoTransactionsException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static uk.co.deliverymind.lightning.shared.TestData.*;

public class JMeterCSVFileReaderTest {

    @Test
    public void verifyReadMethod() {
        JMeterTransactions jmeterTransactions = new JMeterCSVFileReader().getTransactions(CSV_2_TRANSACTIONS);
        assertThat(jmeterTransactions, hasItem(LOGIN_3514_SUCCESS));
        assertThat(jmeterTransactions, hasItem(SEARCH_11221_SUCCESS));
    }

    @Test(expectedExceptions = CSVFileIOException.class)
    public void verifyReadMethodIOException() {
        new JMeterCSVFileReader().getTransactions(CSV_NONEXISTENT);
    }

    @Test(expectedExceptions = CSVFileNoTransactionsException.class)
    public void verifyReadMethodNoTransactionsException() {
        new JMeterCSVFileReader().getTransactions(CSV_0_TRANSACTIONS);
    }
}