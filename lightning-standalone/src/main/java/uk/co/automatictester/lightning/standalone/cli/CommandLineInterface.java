package uk.co.automatictester.lightning.standalone.cli;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.automatictester.lightning.standalone.cli.commands.CommandReport;
import uk.co.automatictester.lightning.standalone.cli.commands.CommandVerify;

import java.io.IOException;
import java.util.Properties;

@Parameters(separators = "=")
public class CommandLineInterface {

    private static final Logger log = LoggerFactory.getLogger(CommandLineInterface.class);

    @Parameter(names = {"-h", "--help"}, help = true, hidden = true)
    private boolean help;

    private JCommander jc;

    public CommandVerify verify;
    public CommandReport report;

    public CommandLineInterface(String[] args) {
        jc = new JCommander(this);
        CommandVerify commandVerify = new CommandVerify();
        CommandReport commandReport = new CommandReport();
        jc.addCommand("verify", commandVerify);
        jc.addCommand("report", commandReport);
        jc.parse(args);
        this.verify = commandVerify;
        this.report = commandReport;
    }

    public boolean isHelpRequested() {
        return help;
    }

    public void printHelp() {
        jc.setProgramName(String.format("java -jar lightning-%s.jar", getVersion()));
        jc.usage();
    }

    public String getParsedCommand() {
        return jc.getParsedCommand();
    }

    public String getVersion() {
        Properties prop = getPropertriesFromFile();
        terminateOnMissingVersion(prop);
        return prop.getProperty("version");
    }

    private Properties getPropertriesFromFile() {
        Properties prop = new Properties();
        try {
            prop.load(getClass().getClassLoader().getResourceAsStream("version.properties"));
        } catch (IOException | NullPointerException e) {
            logErrorAndExit("Error reading version.properties");
        }
        return prop;
    }

    private void terminateOnMissingVersion(Properties prop) {
        String version = prop.getProperty("version");
        if (null == version) {
            logErrorAndExit("Error reading version from version.properties");
        }
    }

    private void logErrorAndExit(String errorMessage) {
        log.error(errorMessage);
        System.exit(1);
    }
}
