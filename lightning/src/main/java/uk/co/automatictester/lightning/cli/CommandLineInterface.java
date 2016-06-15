package uk.co.automatictester.lightning.cli;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import uk.co.automatictester.lightning.cli.commands.CommandReport;
import uk.co.automatictester.lightning.cli.commands.CommandVerify;

@Parameters(separators = "=")
public class CommandLineInterface {

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
        jc.setProgramName("java -jar lightning-<version_number>.jar");
        jc.usage();
    }

    public String getParsedCommand() {
        return jc.getParsedCommand();
    }

}
