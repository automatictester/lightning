package uk.co.automatictester.lightning.standalone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import uk.co.automatictester.lightning.standalone.cli.CliTestRunner;

@SpringBootApplication
public class Application implements ApplicationRunner {

    private final CliTestRunner cliTestRunner;

    public Application(@Autowired CliTestRunner cliTestRunner) {
        this.cliTestRunner = cliTestRunner;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(ApplicationArguments args) {
        cliTestRunner.main(args);
    }
}
