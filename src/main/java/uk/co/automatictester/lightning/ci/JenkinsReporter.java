package uk.co.automatictester.lightning.ci;

import uk.co.automatictester.lightning.TestSet;
import uk.co.automatictester.lightning.data.JMeterTransactions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

public class JenkinsReporter extends CIReporter {

    public JenkinsReporter(TestSet testSet) {
        super(testSet);
    }

    public JenkinsReporter(JMeterTransactions jmeterTransactions) {
        super(jmeterTransactions);
    }

    public void setJenkinsBuildName() {
        if (testSet != null) {
            writeJenkinsFile(getVerifySummary(testSet));
        } else if (jmeterTransactions != null) {
            writeJenkinsFile(getReportSummary(jmeterTransactions));
        }
    }

    private void writeJenkinsFile(String summary) {
        try {
            Properties props = new Properties();
            props.setProperty("result.string", summary);
            File jenkinsBuildNameSetterFile = new File("reports/lightning-jenkins.properties");
            jenkinsBuildNameSetterFile.getParentFile().mkdirs();
            OutputStream out = new FileOutputStream(jenkinsBuildNameSetterFile);
            props.store(out, "In Jenkins Build Name Setter Plugin, define build name as: ${PROPFILE,file=\"lightning-jenkins.properties\",property=\"result.string\"}");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
