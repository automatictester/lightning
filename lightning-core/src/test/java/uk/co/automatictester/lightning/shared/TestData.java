package uk.co.automatictester.lightning.shared;

import uk.co.automatictester.lightning.tests.*;
import uk.co.automatictester.lightning.utils.Percent;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class TestData {

    // Resources
    private static final String RESOURCES = "src/test/resources/";
    private static final String XML_RESOURCES = RESOURCES + "xml/";
    private static final String JMETER_CSV_RESOURCES = RESOURCES + "csv/jmeter/";
    private static final String PERFMON_CSV_RESOURCES = RESOURCES + "csv/perfmon/";

    // XML files
    public static final File TEST_SET_3_0_0 = new File(XML_RESOURCES + "3_0_0.xml");
    public static final File TEST_SET_1_2 = new File(XML_RESOURCES + "1_client_2_server.xml");
    public static final File TEST_SET_1_1_1 = new File(XML_RESOURCES + "1_1_1.xml");
    public static final File TEST_SET_0_0_0 = new File(XML_RESOURCES + "0_0_0.xml");
    public static final File TEST_SET_AVG_RESP_TIME = new File(XML_RESOURCES + "avgRespTimeTest.xml");
    public static final File TEST_SET_MAX_RESP_TIME = new File(XML_RESOURCES + "maxRespTimeTest.xml");
    public static final File TEST_SET_PERCENTILE = new File(XML_RESOURCES + "nthPercRespTimeTest.xml");
    public static final File TEST_SET_PASSED = new File(XML_RESOURCES + "passedTransactionsTest.xml");
    public static final File TEST_SET_PASSED_PERCENT = new File(XML_RESOURCES + "passedTransactionsPercentTest.xml");
    public static final File TEST_SET_STD_DEV = new File(XML_RESOURCES + "respTimeStdDevTest.xml");
    public static final File TEST_SET_THROUGHPUT = new File(XML_RESOURCES + "throughputTest.xml");
    public static final File TEST_SET_MEDIAN = new File(XML_RESOURCES + "medianRespTimeTest.xml");
    public static final File TEST_SET_NOT_WELL_FORMED = new File(XML_RESOURCES + "not_well_formed.xml");
    public static final File TEST_SET_SERVER_LESS = new File(XML_RESOURCES + "serverSideTest_lessThan.xml");
    public static final File TEST_SET_SERVER_BETWEEN = new File(XML_RESOURCES + "serverSideTest_between.xml");
    public static final File TEST_SET_SERVER_GREATER = new File(XML_RESOURCES + "serverSideTest_greaterThan.xml");
    public static final File TEST_SET_XML_FILE_NO_VALID_SUB_TYPE_EXCAPTION = new File(XML_RESOURCES + "XMLFileNoValidSubTypeException.xml");
    public static final File TEST_SET_XML_FILE_NUMBER_FORMAT_EXCEPTION = new File(XML_RESOURCES + "XMLFileNumberFormatException.xml");
    public static final File TEST_SET_XML_FILE_MISSING_ELEMENT_VALUE_EXCEPTION = new File(XML_RESOURCES + "XMLFileMissingElementValueException.xml");
    public static final File TEST_SET_XML_FILE_MISSING_ELEMENT_EXCEPTION = new File(XML_RESOURCES + "XMLFileMissingElementException.xml");
    public static final File TEST_SET_XML_FILE_PERCENTILE_EXCEPTION = new File(XML_RESOURCES + "XMLFilePercentileException.xml");

    // JMeter CSV files
    public static final File CSV_MISSING_LABEL_COLUMN = new File(JMETER_CSV_RESOURCES + "missing_label_column.csv");
    public static final File CSV_NOT_ENOUGH_COLUMNS_IN_DATA_PART = new File(JMETER_CSV_RESOURCES + "not_enough_columns_in_data_part.csv");
    public static final File CSV_2_TRANSACTIONS = new File(JMETER_CSV_RESOURCES + "2_transactions.csv");
    public static final File CSV_0_TRANSACTIONS = new File(JMETER_CSV_RESOURCES + "0_transactions.csv");
    public static final File CSV_NONEXISTENT = new File(JMETER_CSV_RESOURCES + "nonexistent.csv");

    // PerfMon CSV files
    public static final File CSV_2_ENTRIES = new File(PERFMON_CSV_RESOURCES + "2_entries.csv");
    public static final File CSV_0_ENTRIES = new File(PERFMON_CSV_RESOURCES + "0_entries.csv");

    // PerfMon data entries
    public static final ArrayList<String> CPU_ENTRY_9128 = new ArrayList<>(Arrays.asList("1455366135623", "9128", "192.168.0.12 CPU"));
    public static final ArrayList<String> CPU_ENTRY_21250 = new ArrayList<>(Arrays.asList("1455366136635", "21250", "192.168.0.12 CPU"));
    public static final ArrayList<String> CPU_ENTRY_10000 = new ArrayList<>(Arrays.asList("1455366136635", "10000", "192.168.0.12 CPU"));
    public static final ArrayList<String> CPU_ENTRY_10001 = new ArrayList<>(Arrays.asList("1455366136635", "10001", "192.168.0.12 CPU"));
    public static final ArrayList<String> CPU_ENTRY_15000 = new ArrayList<>(Arrays.asList("1455366136635", "15000", "192.168.0.12 CPU"));
    public static final ArrayList<String> CPU_ENTRY_25000 = new ArrayList<>(Arrays.asList("1455366136635", "25000", "192.168.0.12 CPU"));
    public static final ArrayList<String> CPU_ENTRY_30000 = new ArrayList<>(Arrays.asList("1455366136635", "30000", "192.168.0.12 CPU"));

    // Transactions
    public static final ArrayList<String> LOGIN_3514_SUCCESS = new ArrayList<>(Arrays.asList("Login", "3514", "true", "1434291247743"));
    public static final ArrayList<String> SEARCH_11221_SUCCESS = new ArrayList<>(Arrays.asList("Search", "11221", "true", "1434291252072"));

    public static final ArrayList<String> LOGIN_1200_SUCCESS = new ArrayList<>(Arrays.asList("Login", "1200", "true"));
    public static final ArrayList<String> LOGIN_1200_FAILURE = new ArrayList<>(Arrays.asList("Login", "1200", "false"));
    public static final ArrayList<String> LOGIN_1000_SUCCESS = new ArrayList<>(Arrays.asList("Login", "1000", "true"));

    public static final ArrayList<String> SEARCH_800_SUCCESS = new ArrayList<>(Arrays.asList("Search", "800", "true"));
    public static final ArrayList<String> SEARCH_800_FAILURE = new ArrayList<>(Arrays.asList("Search", "800", "false"));
    public static final ArrayList<String> SEARCH_1_SUCCESS = new ArrayList<>(Arrays.asList("Search", "1", "true"));
    public static final ArrayList<String> SEARCH_2_SUCCESS = new ArrayList<>(Arrays.asList("Search", "2", "true"));
    public static final ArrayList<String> SEARCH_3_SUCCESS = new ArrayList<>(Arrays.asList("Search", "3", "true"));
    public static final ArrayList<String> SEARCH_4_SUCCESS = new ArrayList<>(Arrays.asList("Search", "4", "true"));
    public static final ArrayList<String> SEARCH_5_SUCCESS = new ArrayList<>(Arrays.asList("Search", "5", "true"));
    public static final ArrayList<String> SEARCH_6_SUCCESS = new ArrayList<>(Arrays.asList("Search", "6", "true"));
    public static final ArrayList<String> SEARCH_7_SUCCESS = new ArrayList<>(Arrays.asList("Search", "7", "true"));
    public static final ArrayList<String> SEARCH_8_SUCCESS = new ArrayList<>(Arrays.asList("Search", "8", "true"));
    public static final ArrayList<String> SEARCH_9_SUCCESS = new ArrayList<>(Arrays.asList("Search", "9", "true"));
    public static final ArrayList<String> SEARCH_10_SUCCESS = new ArrayList<>(Arrays.asList("Search", "10", "true"));
    public static final ArrayList<String> LOGIN_1_SUCCESS = new ArrayList<>(Arrays.asList("Login", "1", "true"));
    public static final ArrayList<String> LOGIN_2_SUCCESS = new ArrayList<>(Arrays.asList("Login", "2", "true"));

    // Tests
    public static final PassedTransactionsTest PASSED_TRANSACTIONS_TEST_3_0_0_A = new PassedTransactionsTest("Failed transactions (%)", "passedTransactionsTest", "Verify number of passed tests", "Login", new Percent(0));
    public static final PassedTransactionsTest PASSED_TRANSACTIONS_TEST_3_0_0_B = new PassedTransactionsTest("Failed transactions", "passedTransactionsTest", "Verify number of passed tests", null, 0);
    public static final RespTimeNthPercentileTest RESP_TIME_PERC_TEST_3_0_0_C = new RespTimeNthPercentileTest("80th percentile", "nthPercRespTimeTest", "Verify nth percentile", "Search", 80, 11245);

    public static final PassedTransactionsTest PASSED_TRANSACTIONS_TEST_A = new PassedTransactionsTest("Test #1", "passedTransactionsTest", "Verify number of passed tests", "Login", 1);
    public static final PassedTransactionsTest PASSED_TRANSACTIONS_TEST_B = new PassedTransactionsTest("Test #1", "passedTransactionsTest", "Verify number of passed tests", "Login", 0);
    public static final PassedTransactionsTest PASSED_TRANSACTIONS_TEST_PERC = new PassedTransactionsTest("Test #1", "passedTransactionsTest", "Verify number of passed tests", "Login", new Percent(0));
    public static final PassedTransactionsTest PASSED_TRANSACTIONS_TEST_NO_TRANS_NAME = new PassedTransactionsTest("Test #1", "passedTransactionsTest", "Verify number of passed tests", null, 0);
    public static final RespTimeAvgTest AVG_RESP_TIME_TEST_A = new RespTimeAvgTest("Test #1", "avgRespTimeTest", "Verify average response times", "Search", 1000);
    public static final RespTimeAvgTest AVG_RESP_TIME_TEST_B = new RespTimeAvgTest("Test #1", "avgRespTimeTest", "Verify average response times", "Search", 100);
    public static final RespTimeMaxTest MAX_RESP_TIME_TEST_A = new RespTimeMaxTest("Test #1", "maxRespTimeTest", "Verify max response times", "Search", 1000);
    public static final RespTimeMaxTest MAX_RESP_TIME_TEST_B = new RespTimeMaxTest("Test #1", "maxRespTimeTest", "Verify max response times", "Search", 100);
    public static final RespTimeStdDevTest RESP_TIME_STD_DEV_TEST_A = new RespTimeStdDevTest("Test #1", "respTimeStdDevTest", "Verify standard deviation", "Login", 1);
    public static final RespTimeStdDevTest RESP_TIME_STD_DEV_TEST_B = new RespTimeStdDevTest("Test #1", "respTimeStdDevTest", "Verify standard deviation", "Login", 0);
    public static final RespTimeNthPercentileTest RESP_TIME_PERC_TEST_A = new RespTimeNthPercentileTest("Test #1", "nthPercRespTimeTest", "Verify 90 percentile", "Search", 9, 9);
    public static final RespTimeNthPercentileTest RESP_TIME_PERC_TEST_B = new RespTimeNthPercentileTest("Test #1", "nthPercRespTimeTest", "Verify 90 percentile", "Search", 9, 8);
    public static final RespTimeMedianTest RESP_TIME_MEDIAN_TEST_A = new RespTimeMedianTest("Test #1", "medianRespTimeTest", "Verify median", "Search", 9);
    public static final RespTimeMedianTest RESP_TIME_MEDIAN_TEST_B = new RespTimeMedianTest("Test #1", "medianRespTimeTest", "Verify median", "Search", 8);

}
