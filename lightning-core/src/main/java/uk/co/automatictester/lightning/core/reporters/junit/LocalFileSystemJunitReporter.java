package uk.co.automatictester.lightning.core.reporters.junit;

import uk.co.automatictester.lightning.core.exceptions.JunitReportGenerationException;
import uk.co.automatictester.lightning.core.state.tests.TestSet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LocalFileSystemJunitReporter {

    private static String suffix;

    private LocalFileSystemJunitReporter() {
    }

    public static void setSuffix(String suffix) {
        LocalFileSystemJunitReporter.suffix = suffix;
    }

    public static void generateReport(TestSet testSet) {
        JunitReportGenerator reportGenerator = new JunitReportGenerator(testSet);
        String report = reportGenerator.generate();
        storeReportToDisk(report);
    }

    private static void storeReportToDisk(String report) {
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
