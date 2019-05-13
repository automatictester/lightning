package uk.co.automatictester.lightning.core.reporters.junit;

import uk.co.automatictester.lightning.core.exceptions.JunitReportGenerationException;
import uk.co.automatictester.lightning.core.state.tests.TestSet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LocalFileSystemJunitReporter {

    private LocalFileSystemJunitReporter() {
    }

    public static void generateReport(TestSet testSet, String suffix) {
        JunitReportGenerator reportGenerator = new JunitReportGenerator(testSet);
        String report = reportGenerator.generate();
        storeReportToDisk(report, suffix);
    }

    private static void storeReportToDisk(String report, String suffix) {
        String filename = getFilename(suffix);
        Path PATH = Paths.get(filename);
        try {
            Files.write(PATH, report.getBytes());
        } catch (IOException e) {
            throw new JunitReportGenerationException(e);
        }
    }

    static String getFilename(String suffix) {
        return suffix == null ? "junit.xml" : "junit-" + suffix + ".xml";
    }
}
