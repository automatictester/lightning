package uk.co.automatictester.lightning.cli.delegates;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import uk.co.automatictester.lightning.cli.validators.CIServerValidator;

@Parameters(separators = "=")
public class CI {

    @Parameter(names = "-ci", description = "CI server (jenkins or teamcity)", required = false, validateWith = CIServerValidator.class)
    private String ci;

    public boolean isCIEqualTo(String ci) {
        if (this.ci == null) {
            return false;
        } else {
            return this.ci.toLowerCase().equals(ci);
        }
    }

}
