package uk.co.automatictester.lightning.core.reporters.junit;

import org.testng.annotations.Test;
import uk.co.automatictester.lightning.core.state.tests.TestSet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

public class LocalFileSystemJunitReporterTest {

    @Test
    public void testGenerateReportWithoutSuffix() throws IOException {
        LocalFileSystemJunitReporter.generateReport(new TestSet(), null);
        String reportContent = readFileToStringAndDelete(null);
        String expectedReportContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><testsuite errors=\"0\" failures=\"0\" name=\"Lightning\" tests=\"0\" time=\"0\"/>";
        assertThat(reportContent, containsString(expectedReportContent));
    }

    @Test
    public void testGenerateReportWithSuffix() throws IOException {
        String suffix = "abc";
        LocalFileSystemJunitReporter.generateReport(new TestSet(), suffix);
        String reportContent = readFileToStringAndDelete(suffix);
        String expectedReportContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><testsuite errors=\"0\" failures=\"0\" name=\"Lightning\" tests=\"0\" time=\"0\"/>";
        assertThat(reportContent, containsString(expectedReportContent));
    }

    private String readFileToStringAndDelete(String suffix) throws IOException {
        String filename = LocalFileSystemJunitReporter.getFilename(suffix);
        Path path = Paths.get(filename);
        String fileContent = Files.lines(path).collect(Collectors.joining("\n"));
        Files.delete(path);
        return fileContent;
    }
}
