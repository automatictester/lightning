package uk.co.automatictester.lightning.data;

import uk.co.automatictester.lightning.tests.AvgRespTimeTest;
import uk.co.automatictester.lightning.tests.PassedTransactionsTest;
import uk.co.automatictester.lightning.tests.RespTimeStdDevTest;

import java.util.ArrayList;
import java.util.Arrays;

public class TestData {

    // Resources
    private static final String RESOURCES = "src/test/resources/";
    private static final String XML_RESOURCES = RESOURCES + "xml/";
    private static final String CSV_RESOURCES = RESOURCES + "csv/";

    // XML files
    public static final String TEST_SET_3_0_0 = XML_RESOURCES + "3_0_0.xml";
    public static final String TEST_SET_1_1_1 = XML_RESOURCES + "1_1_1.xml";
    public static final String TEST_SET_NOT_VALID = XML_RESOURCES + "not_valid.xml";
    public static final String TEST_SET_NOT_WELL_FORMED = XML_RESOURCES + "not_well_formed.xml";

    // CSV files
    public static final String CSV_MISSING_LABEL_COLUMN = CSV_RESOURCES + "missing_label_column.csv";
    public static final String CSV_2_TRANSACTIONS = CSV_RESOURCES + "2_transactions.csv";
    public static final String CSV_10_TRANSACTIONS = CSV_RESOURCES + "10_transactions.csv";
    public static final String CSV_NONEXISTENT = CSV_RESOURCES + "nonexistent.csv";

    // Transactions
    public static final ArrayList<String> LOGIN_1200_SUCCESS = new ArrayList<>(Arrays.asList("Login", "1200", "true"));
    public static final ArrayList<String> LOGIN_1200_FAILURE = new ArrayList<>(Arrays.asList("Login", "1200", "false"));
    public static final ArrayList<String> LOGIN_1000_SUCCESS = new ArrayList<>(Arrays.asList("Login", "1000", "true"));
    public static final ArrayList<String> LOGIN_3514_SUCCESS = new ArrayList<>(Arrays.asList("Login", "3514", "true"));

    public static final ArrayList<String> SEARCH_11221_SUCCESS = new ArrayList<>(Arrays.asList("Search", "11221", "true"));
    public static final ArrayList<String> SEARCH_800_SUCCESS = new ArrayList<>(Arrays.asList("Search", "800", "true"));
    public static final ArrayList<String> SEARCH_1_SUCCESS = new ArrayList<>(Arrays.asList("Search", "1", "true"));
    public static final ArrayList<String> SEARCH_2_SUCCESS = new ArrayList<>(Arrays.asList("Search", "2", "true"));
    public static final ArrayList<String> SEARCH_3_SUCCESS = new ArrayList<>(Arrays.asList("Search", "3", "true"));

    // Tests
    public static final PassedTransactionsTest PASSED_TRANSACTIONS_TEST_A = new PassedTransactionsTest("Test #1", "Verify number of passed tests", "Login", 1);
    public static final PassedTransactionsTest PASSED_TRANSACTIONS_TEST_B = new PassedTransactionsTest("Test #1", "Verify number of passed tests", "Login", 0);
    public static final AvgRespTimeTest AVG_RESP_TIME_TEST_A = new AvgRespTimeTest("Test #1", "Verify average response times", "Search", 1000);
    public static final AvgRespTimeTest AVG_RESP_TIME_TEST_B = new AvgRespTimeTest("Test #1", "Verify average response times", "Search", 100);
    public static final RespTimeStdDevTest RESP_TIME_STD_DEV_TEST_A = new RespTimeStdDevTest("Test #1", "Verify standard deviation", "Login", 1);
    public static final RespTimeStdDevTest RESP_TIME_STD_DEV_TEST_B = new RespTimeStdDevTest("Test #1", "Verify standard deviation", "Login", 0);

    // Other
    public static final String NONEXISTENT_LABEL = "nonexistent";
    public static final String EXISTING_LABEL = "Login";
    public static final String MISSING_COLUMN_NAME = "label";

}
