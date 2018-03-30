package uk.co.automatictester.lightning.standalone.cli.validators;

import com.beust.jcommander.ParameterException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CIServerValidatorTest {

    private static final String CI = "-ci";

    @DataProvider(name = "testData")
    private String[][] testData() {
        return new String[][]{
                {"jenkins"},
                {"Jenkins"},
                {"teamcity"},
                {"TeamCity"}
        };
    }

    @Test(dataProvider = "testData")
    public void verifyValidateValid(String ci) {
        new CIServerValidator().validate(CI, ci.toLowerCase());
    }

    @Test(expectedExceptions = ParameterException.class, expectedExceptionsMessageRegExp = "CI server 'enkins' not in list: \\[jenkins, teamcity\\]")
    public void verifyValidateInvalid() {
        new CIServerValidator().validate(CI, "enkins");
    }
}