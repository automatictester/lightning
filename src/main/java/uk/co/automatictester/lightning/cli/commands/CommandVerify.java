package uk.co.automatictester.lightning.cli.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.ParametersDelegate;
import uk.co.automatictester.lightning.cli.delegates.CI;
import uk.co.automatictester.lightning.cli.delegates.JmeterCsvFile;
import uk.co.automatictester.lightning.cli.validators.FileValidator;

@Parameters(separators = "=", commandDescription = "Execute Lightning tests against JMeter output")
public class CommandVerify {

    @Parameter(names = "-xml", description = "Lightning XML config file", required = true, validateWith = FileValidator.class)
    private String xmlFile;

    @Parameter(names = "--perfmon-csv", description = "PerfMon CSV result file", required = false, validateWith = FileValidator.class)
    private String perfmonCsvFile;

    @ParametersDelegate
    private JmeterCsvFile jmeterCsvFile = new JmeterCsvFile();

    @ParametersDelegate
    private CI ci = new CI();

    public String getXmlFile() {
        return xmlFile;
    }

    public String getJmeterCsvFile() {
        return jmeterCsvFile.getJmeterCsvFile();
    }

    public String getPerfmonCsvFile() {
        return perfmonCsvFile;
    }

    public boolean isCiEqualTo(String ci) {
        return this.ci.isCIEqualTo(ci);
    }

    public boolean isPerfmonCsvFileProvided() {
        return perfmonCsvFile != null;
    }
}
