package uk.co.automatictester.lightning.cli.commands;

import com.beust.jcommander.Parameters;
import com.beust.jcommander.ParametersDelegate;
import uk.co.automatictester.lightning.cli.delegates.CI;
import uk.co.automatictester.lightning.cli.delegates.JmeterCsvFile;

@Parameters(separators = "=", commandDescription = "Generate report on JMeter output")
public class CommandReport {

    @ParametersDelegate
    private JmeterCsvFile jmeterCsvFile = new JmeterCsvFile();

    @ParametersDelegate
    private CI ci = new CI();

    public String getJmeterCsvFile() {
        return jmeterCsvFile.getJmeterCsvFile();
    }

    public boolean isCiEqualTo(String ci) {
        return this.ci.isCIEqualTo(ci);
    }
}
