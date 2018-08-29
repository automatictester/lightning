package uk.co.automatictester.lightning.core.ci;

import uk.co.automatictester.lightning.core.state.TestSet;
import uk.co.automatictester.lightning.core.data.JMeterTransactions;
import uk.co.automatictester.lightning.core.exceptions.JenkinsReportGenerationException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class JenkinsReporter extends CIReporter {

    private JenkinsReporter(TestSet testSet) {
        super(testSet);
    }

    private JenkinsReporter(JMeterTransactions jmeterTransactions) {
        super(jmeterTransactions);
    }

    public static JenkinsReporter fromTestSet(TestSet testSet) {
        return new JenkinsReporter(testSet);
    }

    public static JenkinsReporter fromJmeterTransactions(JMeterTransactions jmeterTransactions) {
        return new JenkinsReporter(jmeterTransactions);
    }

    public void setJenkinsBuildName() {
        String fileContent = null;
        if (testSet != null) {
            fileContent = getVerifySummary();
        } else if (jmeterTransactions != null) {
            fileContent = getReportSummary();
        }
        writeJenkinsBuildNameSetterFile(fileContent);
    }

    private String getVerifySummary() {
        int executed = testSet.getTestCount();
        int failed = testSet.getFailCount() + testSet.getErrorCount();
        return String.format("Tests executed: %s, failed: %s", executed, failed);
    }

    private void writeJenkinsBuildNameSetterFile(String summary) {
        try (FileOutputStream fos = new FileOutputStream("lightning-jenkins.properties")) {
            Properties props = new Properties();
            props.setProperty("result.string", summary);
            OutputStreamWriter out = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            props.store(out, "In Jenkins Build Name Setter Plugin, define build name as: ${BUILD_NUMBER} - ${PROPFILE,file=\"lightning-jenkins.properties\",property=\"result.string\"}");
        } catch (IOException e) {
            throw new JenkinsReportGenerationException(e);
        }
    }
}
