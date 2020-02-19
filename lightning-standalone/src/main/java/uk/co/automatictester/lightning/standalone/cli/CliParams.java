package uk.co.automatictester.lightning.standalone.cli;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Slf4j
public class CliParams {

    private final ApplicationArguments args;

    public CliParams(ApplicationArguments args) {
        this.args = args;
    }

    public File getXmlFile() {
        return getFileFromOptionValue("xml");
    }

    public File getJmeterCsvFile() {
        return getFileFromOptionValue("jmeter-csv");
    }

    public File getPerfmonCsvFile() {
        return getFileFromOptionValue("perfmon-csv");
    }

    public boolean isPerfmonCsvFileProvided() {
        String perfMonCsv = "perfmon-csv";
        return args.containsOption(perfMonCsv) && args.getOptionValues(perfMonCsv).size() == 1;
    }

    public Optional<String> getParsedCommand() {
        List<String> nonOptionArgs = args.getNonOptionArgs();
        if (nonOptionArgs != null && nonOptionArgs.size() == 1 && nonOptionArgs.get(0).equals("report")) {
            return Optional.of("report");
        } else if (nonOptionArgs != null && nonOptionArgs.size() == 1 && nonOptionArgs.get(0).equals("verify")) {
            return Optional.of("verify");
        } else {
            return Optional.empty();
        }
    }

    public boolean isHelpRequested() {
        return args.containsOption("h") || args.containsOption("help");
    }

    File getFileFromOptionValue(String optionName) {
        if (args.containsOption(optionName) && args.getOptionValues(optionName).size() == 1) {
            String optionValue = args.getOptionValues(optionName).get(0);
            if (!new File(optionValue).canRead()) {
                throw new RuntimeException("Error reading file: " + optionValue);
            }
            return new File(optionValue);
        }
        throw new RuntimeException("Number of option values for '" + optionName + "' not equal to 1");
    }
}
