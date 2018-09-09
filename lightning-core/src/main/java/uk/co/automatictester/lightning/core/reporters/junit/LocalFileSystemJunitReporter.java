package uk.co.automatictester.lightning.core.reporters.junit;

import uk.co.automatictester.lightning.core.exceptions.JunitReportGenerationException;
import uk.co.automatictester.lightning.core.state.tests.TestSet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LocalFileSystemJunitReporter {

    private static final Path PATH = Paths.get("junit.xml");

    private LocalFileSystemJunitReporter() {
    }

    public static void generateReport(TestSet testSet) {
        JunitReport junitReport = new JunitReport(testSet);
        String report = junitReport.generateReportContent();
        storeReportToDisk(report);
    }

    private static void storeReportToDisk(String report) {
        try {
            Files.write(PATH, report.getBytes());
        } catch (IOException e) {
            throw new JunitReportGenerationException(e);
        }
    }
}
