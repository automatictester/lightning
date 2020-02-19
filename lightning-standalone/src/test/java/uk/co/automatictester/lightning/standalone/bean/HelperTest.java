package uk.co.automatictester.lightning.standalone.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ContextConfiguration(classes = Helper.class)
public class HelperTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private Helper helper;

    @Test
    public void printHelpTest() {
        String expectedOutput = String.format("Usage: java -jar lightning-%s.jar [command] [command options]%n" +
                "  Commands:%n" +
                "    verify      Execute Lightning tests against JMeter output%n" +
                "      Usage: verify [options]%n" +
                "        Options:%n" +
                "        * --jmeter-csv%n" +
                "            JMeter CSV result file%n" +
                "          --perfmon-csv%n" +
                "            PerfMon CSV result file%n" +
                "        * --xml%n" +
                "            Lightning XML config file%n" +
                "%n" +
                "    report      Generate report on JMeter output%n" +
                "      Usage: report [options]%n" +
                "        Options:%n" +
                "        * --jmeter-csv%n" +
                "            JMeter CSV result file%n", helper.getVersion());
        configureStream();
        helper.printHelp();
        String actual = out.toString();
        if (System.getProperty("os.name").startsWith("Windows")) {
            actual = actual.replace("\n", "\r\n");
        }
        assertThat(actual).isEqualTo(expectedOutput);
        revertStream();
    }

    private final ByteArrayOutputStream out = new ByteArrayOutputStream();

    private void configureStream() {
        System.setOut(new PrintStream(out));
    }

    private void revertStream() {
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
    }
}
