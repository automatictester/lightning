package uk.co.deliverymind.lightning.standalone.cli;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.deliverymind.lightning.standalone.cli.commands.CommandReport;
import uk.co.deliverymind.lightning.standalone.cli.commands.CommandVerify;

import java.io.IOException;
import java.util.Properties;

@Parameters(separators = "=")
public class CommandLineInterface {

    private static final Logger logger = LoggerFactory.getLogger(CommandLineInterface.class);

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

    private String getVersion() {
        Properties prop = new Properties();
        try {
            prop.load(getClass().getClassLoader().getResourceAsStream("version.properties"));
        } catch (IOException | NullPointerException e) {
            logger.error("Error reading version.properties");
            System.exit(1);
        }
        String version = prop.getProperty("version");
        if (null == version) {
            logger.error("Error reading version from version.properties");
            System.exit(1);
        }
        return version;
    }

}
