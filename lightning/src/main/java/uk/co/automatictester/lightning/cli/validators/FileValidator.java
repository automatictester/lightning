package uk.co.automatictester.lightning.cli.validators;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

import java.io.File;

public class FileValidator implements IParameterValidator {

    public void validate(String name, String value) throws ParameterException {
        File f = new File(value);
        if (!f.canRead()) {
            throw new ParameterException("Error reading file: " + value);
        }
    }
}
