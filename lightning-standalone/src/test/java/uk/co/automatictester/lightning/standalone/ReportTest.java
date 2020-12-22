package uk.co.automatictester.lightning.standalone;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@SpringBootTest(args = {"report", "--jmeter-csv=src/test/resources/csv/jmeter/10_transactions.csv"})
public class ReportTest extends AbstractTestNGSpringContextTests {

    @Test
    public void testReport() {
    }
}
