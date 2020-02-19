package uk.co.automatictester.lightning.standalone.bean;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Slf4j
@Component
public class Helper {

    public void printHelp() {
        String rawHelp = readHelpFile();
        String help = rawHelp.replace("VERSION_PLACEHODER", getVersion());
        log.info(help);
    }

    String getVersion() {
        String version = getClass().getPackage().getImplementationVersion();
        return version == null ? "<version_number>" : version;
    }

    private String readHelpFile() {
        return new Scanner(getClass().getResourceAsStream("/help.txt"), "UTF-8").useDelimiter("\\A").next();
    }
}
