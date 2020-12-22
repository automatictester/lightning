package uk.co.automatictester.lightning.standalone.cli.commands;

import com.beust.jcommander.Parameters;
import com.beust.jcommander.ParametersDelegate;
import uk.co.automatictester.lightning.standalone.cli.delegates.JmeterCsvFile;

import java.io.File;

@Parameters(separators = "=", commandDescription = "Generate report on JMeter output")
public class CommandReport {

    @ParametersDelegate
    private JmeterCsvFile jmeterCsvFile = new JmeterCsvFile();

    public File getJmeterCsvFile() {
        return jmeterCsvFile.getJmeterCsvFile();
    }
}
