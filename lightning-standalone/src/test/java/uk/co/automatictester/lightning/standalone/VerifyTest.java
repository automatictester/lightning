package uk.co.automatictester.lightning.standalone;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@SpringBootTest(args = {
        "verify",
        "--xml=src/test/resources/xml/3_0_0.xml",
        "--jmeter-csv=src/test/resources/csv/jmeter/10_transactions.csv"
})
public class VerifyTest extends AbstractTestNGSpringContextTests {

    @Test
    public void testVerify() {
    }
}
