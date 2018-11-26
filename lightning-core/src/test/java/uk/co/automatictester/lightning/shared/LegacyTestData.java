package uk.co.automatictester.lightning.shared;

import uk.co.automatictester.lightning.core.tests.PassedTransactionsAbsoluteTest;
import uk.co.automatictester.lightning.core.tests.PassedTransactionsRelativeTest;
import uk.co.automatictester.lightning.core.tests.RespTimeNthPercentileTest;

import java.io.File;

@Deprecated
public class LegacyTestData {

    // Resources
    private static final String RESOURCES = "src/test/resources/";
    private static final String JMETER_CSV_RESOURCES = RESOURCES + "csv/jmeter/";
    private static final String PERFMON_CSV_RESOURCES = RESOURCES + "csv/perfmon/";

    // JMeter CSV files
    public static final File CSV_2_TRANSACTIONS = new File(JMETER_CSV_RESOURCES + "2_transactions.csv");
    public static final File CSV_0_TRANSACTIONS = new File(JMETER_CSV_RESOURCES + "0_transactions.csv");
    public static final File CSV_NONEXISTENT = new File(JMETER_CSV_RESOURCES + "nonexistent.csv");

    // PerfMon CSV files
    public static final File CSV_2_ENTRIES = new File(PERFMON_CSV_RESOURCES + "2_entries.csv");
    public static final File CSV_0_ENTRIES = new File(PERFMON_CSV_RESOURCES + "0_entries.csv");

    // PerfMon data entries
    public static final String[] CPU_ENTRY_9128 = new String[]{"1455366135623", "9128", "192.168.0.12 CPU"};
    public static final String[] CPU_ENTRY_21250 = new String[]{"1455366136635", "21250", "192.168.0.12 CPU"};
    public static final String[] CPU_ENTRY_10000 = new String[]{"1455366136635", "10000", "192.168.0.12 CPU"};
    public static final String[] CPU_ENTRY_10001 = new String[]{"1455366136635", "10001", "192.168.0.12 CPU"};
    public static final String[] CPU_ENTRY_15000 = new String[]{"1455366136635", "15000", "192.168.0.12 CPU"};
    public static final String[] CPU_ENTRY_25000 = new String[]{"1455366136635", "25000", "192.168.0.12 CPU"};
    public static final String[] CPU_ENTRY_30000 = new String[]{"1455366136635", "30000", "192.168.0.12 CPU"};

    // Transactions
    public static final String[] LOGIN_3514_SUCCESS = {"Login", "3514", "true", "1434291247743"};
    public static final String[] SEARCH_11221_SUCCESS = {"Search", "11221", "true", "1434291252072"};

    public static final String[] LOGIN_1200_SUCCESS = new String[]{"Login", "1200", "true"};
    public static final String[] LOGIN_1200_FAILURE = new String[]{"Login", "1200", "false"};
    public static final String[] LOGIN_1000_SUCCESS = new String[]{"Login", "1000", "true"};
    public static final String[] LOGOUT_1000_SUCCESS = new String[]{"Logout", "1000", "true"};
    public static final String[] LOGOUT_1000_FAILURE = new String[]{"Logout", "1000", "false"};

    public static final String[] SEARCH_800_SUCCESS = new String[]{"Search", "800", "true"};
    public static final String[] SEARCH_800_FAILURE = new String[]{"Search", "800", "false"};
    public static final String[] SEARCH_1_SUCCESS = new String[]{"Search", "1", "true"};
    public static final String[] SEARCH_2_SUCCESS = new String[]{"Search", "2", "true"};
    public static final String[] SEARCH_3_SUCCESS = new String[]{"Search", "3", "true"};
    public static final String[] SEARCH_4_SUCCESS = new String[]{"Search", "4", "true"};
    public static final String[] SEARCH_5_SUCCESS = new String[]{"Search", "5", "true"};
    public static final String[] SEARCH_6_SUCCESS = new String[]{"Search", "6", "true"};
    public static final String[] SEARCH_7_SUCCESS = new String[]{"Search", "7", "true"};
    public static final String[] SEARCH_8_SUCCESS = new String[]{"Search", "8", "true"};
    public static final String[] SEARCH_9_SUCCESS = new String[]{"Search", "9", "true"};

    // Tests
    public static final PassedTransactionsRelativeTest PASSED_TRANSACTIONS_TEST_3_0_0_A = new PassedTransactionsRelativeTest.Builder("Failed transactions (%)", 0).withDescription("Verify number of passed tests").withTransactionName("Login").build();
    public static final PassedTransactionsAbsoluteTest PASSED_TRANSACTIONS_TEST_3_0_0_B = new PassedTransactionsAbsoluteTest.Builder("Failed transactions", 0).withDescription("Verify number of passed tests").build();
    public static final RespTimeNthPercentileTest RESP_TIME_PERC_TEST_3_0_0_C = new RespTimeNthPercentileTest.Builder("80th percentile", 11245, 80).withDescription("Verify nth percentile").withTransactionName("Search").build();
}
