package uk.co.automatictester.lightning.standalone.cli.validators;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

import java.util.ArrayList;
import java.util.List;

public class CIServerValidator implements IParameterValidator {

    @Override
    public void validate(String name, String value) {
        List<String> ciServers = new ArrayList<>();
        ciServers.add("jenkins");
        ciServers.add("teamcity");

        if (!ciServers.contains(value.toLowerCase())) {
            String errorMessage = String.format("CI server '%s' not in list: %s", value, ciServers.toString());
            throw new ParameterException(errorMessage);
        }
    }
}
