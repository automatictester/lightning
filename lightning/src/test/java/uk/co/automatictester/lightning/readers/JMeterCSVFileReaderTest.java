package uk.co.automatictester.lightning.readers;

import org.testng.annotations.Test;
import uk.co.automatictester.lightning.data.JMeterTransactions;
import uk.co.automatictester.lightning.exceptions.CSVFileIOException;
import uk.co.automatictester.lightning.exceptions.CSVFileMalformedDataException;
import uk.co.automatictester.lightning.exceptions.CSVFileMissingColumnNameException;
import uk.co.automatictester.lightning.exceptions.CSVFileNoTransactionsException;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.co.automatictester.lightning.shared.TestData.*;

public class JMeterCSVFileReaderTest {

    @Test
    public void verifyReadMethod() {
        JMeterTransactions jmeterTransactions = new JMeterCSVFileReader().getTransactions(CSV_2_TRANSACTIONS);
        assertThat(jmeterTransactions, hasItem(LOGIN_3514_SUCCESS));
        assertThat(jmeterTransactions, hasItem(SEARCH_11221_SUCCESS));
    }

    @Test(expectedExceptions = CSVFileMissingColumnNameException.class)
    public void verifyReadMethodMissingColumnNameException() {
        new JMeterCSVFileReader().getTransactions(CSV_MISSING_LABEL_COLUMN);
    }

    @Test(expectedExceptions = CSVFileIOException.class)
    public void verifyReadMethodIOException() {
        new JMeterCSVFileReader().getTransactions(CSV_NONEXISTENT);
    }

    @Test(expectedExceptions = CSVFileNoTransactionsException.class)
    public void verifyReadMethodNoTransactionsException() {
        new JMeterCSVFileReader().getTransactions(CSV_0_TRANSACTIONS);
    }

    @Test(expectedExceptions = CSVFileMalformedDataException.class)
    public void verifyReadMethodMalformedDataException() {
        new JMeterCSVFileReader().getTransactions(CSV_NOT_ENOUGH_COLUMNS_IN_DATA_PART);
    }
}