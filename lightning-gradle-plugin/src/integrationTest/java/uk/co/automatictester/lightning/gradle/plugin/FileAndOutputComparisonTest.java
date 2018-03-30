package uk.co.deliverymind.lightning.gradle.plugin;

import org.gradle.internal.impldep.org.apache.commons.io.FileUtils;
import org.gradle.testkit.runner.BuildResult;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

class FileAndOutputComparisonTest {

    boolean fileContentIsEqual(String resourceFilePath, String filePath) throws IOException {
        File resourceFile = new File(this.getClass().getResource(resourceFilePath).getFile());
        return FileUtils.contentEquals(resourceFile, new File(filePath));
    }

    boolean taskOutputContainsFileContent(String resourceFilePath, BuildResult buildResult) {
        String resourceFileContent = new Scanner(this.getClass().getResourceAsStream(resourceFilePath)).useDelimiter("\\A").next();
        String filteredBuildOutput = buildResult.getOutput().replaceAll("Execution time:\\s*\\d*[ms]*\\s", "");
        return filteredBuildOutput.contains(resourceFileContent);
    }

    boolean taskOutputContainsText(String text, BuildResult buildResult) {
        return buildResult.getOutput().contains(text);
    }
}
