package uk.co.automatictester.lightning.standalone.cli.delegates;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import uk.co.automatictester.lightning.standalone.cli.validators.FileValidator;

import java.io.File;

@Parameters(separators = "=")
public class JmeterCsvFile {

    @Parameter(names = "--jmeter-csv", description = "JMeter CSV result file", required = true, validateWith = FileValidator.class)
    private File jmeterCsvFile;

    public File getJmeterCsvFile() {
        return jmeterCsvFile;
    }

}
