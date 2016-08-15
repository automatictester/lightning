package uk.co.automatictester.lightning.standalone.cli.validators;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

import java.io.File;

public class FileValidator implements IParameterValidator {

    @Override
    public void validate(String name, String value) {
        File f = new File(value);
        if (!f.canRead()) {
            throw new ParameterException("Error reading file: " + value);
        }
    }
}
