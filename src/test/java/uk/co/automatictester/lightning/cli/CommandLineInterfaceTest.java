package uk.co.automatictester.lightning.cli;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import uk.co.automatictester.lightning.ConsoleOutputTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CommandLineInterfaceTest extends ConsoleOutputTest {

    @DataProvider(name = "teamcity")
    private String[][] teamCity() {
        return new String[][]{
                {"teamcity"},
                {"TeamCity"}
        };
    }

    @Test(dataProvider = "teamcity")
    public void testIsCIEqualToTeamCityTrue_verify(String ci) {
        CommandLineInterface params = new CommandLineInterface(new String[]{"verify", String.format("-ci=%s", ci), "-xml=src/test/resources/xml/3_0_0.xml", "--jmeter-csv=src/test/resources/csv/jmeter/10_transactions.csv"});
        assertThat(params.verify.isCiEqualTo("teamcity"), is(true));
    }

    @Test(dataProvider = "teamcity")
    public void testIsCIEqualToTeamCityTrue_report(String ci) {
        CommandLineInterface params = new CommandLineInterface(new String[]{"report", String.format("-ci=%s", ci), "--jmeter-csv=src/test/resources/csv/jmeter/10_transactions.csv"});
        assertThat(params.report.isCiEqualTo("teamcity"), is(true));
    }

    @DataProvider(name = "jenkins")
    private String[][] jenkins() {
        return new String[][]{
                {"jenkins"},
                {"Jenkins"}
        };
    }

    @Test(dataProvider = "jenkins")
    public void testIsCIEqualToJenkinsTrue_verify(String ci) {
        CommandLineInterface params = new CommandLineInterface(new String[]{"verify", String.format("-ci=%s", ci), "-xml=src/test/resources/xml/3_0_0.xml", "--jmeter-csv=src/test/resources/csv/jmeter/10_transactions.csv"});
        assertThat(params.verify.isCiEqualTo("jenkins"), is(true));
    }

    @Test(dataProvider = "jenkins")
    public void testIsCIEqualToJenkinsTrue_report(String ci) {
        CommandLineInterface params = new CommandLineInterface(new String[]{"report", String.format("-ci=%s", ci), "--jmeter-csv=src/test/resources/csv/jmeter/10_transactions.csv"});
        assertThat(params.report.isCiEqualTo("jenkins"), is(true));
    }

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
        assertThat(params.report.getJmeterCsvFile(), equalToIgnoringCase("src/test/resources/csv/jmeter/10_transactions.csv"));
    }

    @Test
    public void testGetCSVFileInVerifyMode() {
        CommandLineInterface params = new CommandLineInterface(new String[]{"verify", "-xml=src/test/resources/xml/3_0_0.xml", "--jmeter-csv=src/test/resources/csv/jmeter/10_transactions.csv"});
        assertThat(params.verify.getJmeterCsvFile(), equalToIgnoringCase("src/test/resources/csv/jmeter/10_transactions.csv"));
    }

    @Test
    public void testGetXMLFileInVerifyMode() {
        CommandLineInterface params = new CommandLineInterface(new String[]{"verify", "-xml=src/test/resources/xml/3_0_0.xml", "--jmeter-csv=src/test/resources/csv/jmeter/10_transactions.csv"});
        assertThat(params.verify.getXmlFile(), equalToIgnoringCase("src/test/resources/xml/3_0_0.xml"));
    }

    @Test
    public void testIsPerfmonCsvFileProvided() {
        CommandLineInterface params = new CommandLineInterface(new String[]{"verify", "-xml=src/test/resources/xml/3_0_0.xml", "--jmeter-csv=src/test/resources/csv/jmeter/10_transactions.csv", "--perfmon-csv=src/test/resources/csv/perfmon/2_entries.csv"});
        assertThat(params.verify.isPerfmonCsvFileProvided(), equalTo(true));
    }

    @Test
    public void testGetPerfmonCsvFile() {
        CommandLineInterface params = new CommandLineInterface(new String[]{"verify", "-xml=src/test/resources/xml/3_0_0.xml", "--jmeter-csv=src/test/resources/csv/jmeter/10_transactions.csv", "--perfmon-csv=src/test/resources/csv/perfmon/2_entries.csv"});
        assertThat(params.verify.getPerfmonCsvFile(), equalToIgnoringCase("src/test/resources/csv/perfmon/2_entries.csv"));
    }

    @Test
    public void testIsCIEqualToJenkinsNotSet() {
        CommandLineInterface params = new CommandLineInterface(new String[]{"verify", "-xml=src/test/resources/xml/3_0_0.xml", "--jmeter-csv=src/test/resources/csv/jmeter/10_transactions.csv"});
        assertThat(params.verify.isCiEqualTo("jenkins"), is(false));
    }

    @Test
    public void testIsHelpRequested() {
        CommandLineInterface params = new CommandLineInterface(new String[]{"-h"});
        assertThat(params.isHelpRequested(), is(true));
    }

    @Test
    public void testPrintHelp() {
        String expectedOutput = String.format("Usage: java -jar lightning-<version_number>.jar [options] [command] [command options]%n" +
                "  Commands:%n" +
                "    verify      Execute Lightning tests against JMeter output%n" +
                "      Usage: verify [options]%n" +
                "        Options:%n" +
                "        * --jmeter-csv%n" +
                "             JMeter CSV result file%n" +
                "          --perfmon-csv%n" +
                "             PerfMon CSV result file%n" +
                "          -ci%n" +
                "             CI server (jenkins or teamcity)%n" +
                "        * -xml%n" +
                "             Lightning XML config file%n" +
                "%n" +
                "    report      Generate report on JMeter output%n" +
                "      Usage: report [options]%n" +
                "        Options:%n" +
                "        * --jmeter-csv%n" +
                "             JMeter CSV result file%n" +
                "          -ci%n" +
                "             CI server (jenkins or teamcity)");

        CommandLineInterface params = new CommandLineInterface(new String[]{});
        configureStream();
        params.printHelp();
        String actual = out.toString();
        if (System.getProperty("os.name").startsWith("Windows")) {
            actual = actual.replace("\n", "\r\n");
        }
        assertThat(actual, containsString(expectedOutput));
        revertStream();
    }
}