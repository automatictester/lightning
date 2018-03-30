package uk.co.automatictester.lightning.standalone.cli;

import org.testng.annotations.Test;
import uk.co.automatictester.lightning.standalone.ConsoleOutputTest;

import java.io.File;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CommandLineInterfaceTest extends ConsoleOutputTest {

    @Test
    public void testGetParsedCommandVerify() {
        CommandLineInterface params = new CommandLineInterface(new String[]{"verify", "-xml=src/test/resources/xml/3_0_0.xml", "--jmeter-csv=src/test/resources/csv/jmeter/10_transactions.csv"});
        assertThat(params.getParsedCommand(), equalToIgnoringCase("verify"));
    }

    @Test
    public void testGetParsedCommandReport() {
        CommandLineInterface params = new CommandLineInterface(new String[]{"report", "--jmeter-csv=src/test/resources/csv/jmeter/10_transactions.csv"});
        assertThat(params.getParsedCommand(), equalToIgnoringCase("report"));
    }

    @Test
    public void testGetCSVFileInReportMode() {
        CommandLineInterface params = new CommandLineInterface(new String[]{"report", "--jmeter-csv=src/test/resources/csv/jmeter/10_transactions.csv"});
        assertThat(params.report.getJmeterCsvFile(), is(equalTo((new File("src/test/resources/csv/jmeter/10_transactions.csv")))));
    }

    @Test
    public void testGetCSVFileInVerifyMode() {
        CommandLineInterface params = new CommandLineInterface(new String[]{"verify", "-xml=src/test/resources/xml/3_0_0.xml", "--jmeter-csv=src/test/resources/csv/jmeter/10_transactions.csv"});
        assertThat(params.verify.getJmeterCsvFile(), is(equalTo((new File("src/test/resources/csv/jmeter/10_transactions.csv")))));
    }

    @Test
    public void testGetXMLFileInVerifyMode() {
        CommandLineInterface params = new CommandLineInterface(new String[]{"verify", "-xml=src/test/resources/xml/3_0_0.xml", "--jmeter-csv=src/test/resources/csv/jmeter/10_transactions.csv"});
        assertThat(params.verify.getXmlFile(), is(equalTo((new File("src/test/resources/xml/3_0_0.xml")))));
    }

    @Test
    public void testIsPerfmonCsvFileProvided() {
        CommandLineInterface params = new CommandLineInterface(new String[]{"verify", "-xml=src/test/resources/xml/3_0_0.xml", "--jmeter-csv=src/test/resources/csv/jmeter/10_transactions.csv", "--perfmon-csv=src/test/resources/csv/perfmon/2_entries.csv"});
        assertThat(params.verify.isPerfmonCsvFileProvided(), equalTo(true));
    }

    @Test
    public void testGetPerfmonCsvFile() {
        CommandLineInterface params = new CommandLineInterface(new String[]{"verify", "-xml=src/test/resources/xml/3_0_0.xml", "--jmeter-csv=src/test/resources/csv/jmeter/10_transactions.csv", "--perfmon-csv=src/test/resources/csv/perfmon/2_entries.csv"});
        assertThat(params.verify.getPerfmonCsvFile(), is(equalTo((new File("src/test/resources/csv/perfmon/2_entries.csv")))));
    }

    @Test
    public void testIsHelpRequested() {
        CommandLineInterface params = new CommandLineInterface(new String[]{"-h"});
        assertThat(params.isHelpRequested(), is(true));
    }

    @Test
    public void testPrintHelp() {
        CommandLineInterface params = new CommandLineInterface(new String[]{});
        String expectedOutput = String.format("Usage: java -jar lightning-%s.jar [options] [command] [command options]%n" +
                "  Commands:%n" +
                "    verify      Execute Lightning tests against JMeter output%n" +
                "      Usage: verify [options]%n" +
                "        Options:%n" +
                "        * --jmeter-csv%n" +
                "             JMeter CSV result file%n" +
                "          --perfmon-csv%n" +
                "             PerfMon CSV result file%n" +
                "        * -xml%n" +
                "             Lightning XML config file%n" +
                "%n" +
                "    report      Generate report on JMeter output%n" +
                "      Usage: report [options]%n" +
                "        Options:%n" +
                "        * --jmeter-csv%n" +
                "             JMeter CSV result file%n", params.getVersion());
        configureStream();
        params.printHelp();
        String actual = out.toString();
        if (System.getProperty("os.name").startsWith("Windows")) {
            actual = actual.replace("\n", "\r\n");
        }
        assertThat(actual, equalToIgnoringWhiteSpace(expectedOutput));
        revertStream();
    }
}