package uk.co.automatictester.lightning.core.facade;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.Scanner;

public abstract class FileAndOutputComparisonIT {

    protected final ByteArrayOutputStream out = new ByteArrayOutputStream();

    protected void configureStream() {
        System.setOut(new PrintStream(out));
    }

    protected void revertStream() {
        out.reset();
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
    }

    boolean fileContentIsEqual(String resourceFilePath, String filePath) throws IOException {
        File resourceFile = new File(this.getClass().getResource(resourceFilePath).getFile());
        return FileUtils.contentEquals(resourceFile, new File(filePath));
    }

    boolean taskOutputContainsFileContent(String resourceFilePath) {
        String resourceFileContent = new Scanner(this.getClass().getResourceAsStream(resourceFilePath)).useDelimiter("\\A").next();
        String filteredBuildOutput = out.toString().replaceAll("Execution time:\\s*\\d*[ms]*\\s", "");
        return filteredBuildOutput.contains(resourceFileContent);
    }
}
