package uk.co.automatictester.lightning.standalone.cli.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.ParametersDelegate;
import uk.co.automatictester.lightning.standalone.cli.delegates.JmeterCsvFile;
import uk.co.automatictester.lightning.standalone.cli.validators.FileValidator;

import java.io.File;

@Parameters(separators = "=", commandDescription = "Execute Lightning tests against JMeter output")
public class CommandVerify {

    @Parameter(names = "-xml", description = "Lightning XML config file", required = true, validateWith = FileValidator.class)
    private File xmlFile;

    @Parameter(names = "--perfmon-csv", description = "PerfMon CSV result file", required = false, validateWith = FileValidator.class)
    private File perfmonCsvFile;

    @ParametersDelegate
    private JmeterCsvFile jmeterCsvFile = new JmeterCsvFile();

    public File getXmlFile() {
        return xmlFile;
    }

    public File getJmeterCsvFile() {
        return jmeterCsvFile.getJmeterCsvFile();
    }

    public File getPerfmonCsvFile() {
        return perfmonCsvFile;
    }

    public boolean isPerfmonCsvFileProvided() {
        return perfmonCsvFile != null;
    }
}
