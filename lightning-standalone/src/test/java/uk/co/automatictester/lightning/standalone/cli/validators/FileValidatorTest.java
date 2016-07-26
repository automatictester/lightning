package uk.co.automatictester.lightning.standalone.cli.validators;

import com.beust.jcommander.ParameterException;
import org.testng.annotations.Test;

import java.io.File;

public class FileValidatorTest {

    private static final String JMETER_CSV_RESOURCES = "src/test/resources/csv/jmeter/";
    private static final File CSV_2_TRANSACTIONS = new File(JMETER_CSV_RESOURCES + "2_transactions.csv");
    private static final File CSV_NONEXISTENT = new File(JMETER_CSV_RESOURCES + "nonexistent.csv");

    @Test
    public void verifyValidateExisting() {
        new FileValidator().validate("-csv", CSV_2_TRANSACTIONS.toString());
    }

    @Test(expectedExceptions = ParameterException.class, expectedExceptionsMessageRegExp = "Error reading file: src/test/resources/csv/jmeter/nonexistent.csv")
    public void verifyValidateNonexistent() {
        new FileValidator().validate("-csv", CSV_NONEXISTENT.toString());
    }
}