package uk.co.automatictester.lightning.gradle.extension;

import java.io.File;

public class LightningExtension {

    private File testSetXml;
    private File jmeterCsv;
    private File perfmonCsv;

    public boolean hasAllVerifyInput() {
        return isNotNullAndReadable(jmeterCsv) && isNotNullAndReadable(testSetXml);
    }

    public boolean hasAllReportInput() {
        return isNotNullAndReadable(jmeterCsv);
    }

    public File getPerfmonCsv() {
        return perfmonCsv;
    }

    public void setPerfmonCsv(File perfmonCsv) {
        this.perfmonCsv = perfmonCsv;
    }

    public File getTestSetXml() {
        return testSetXml;
    }

    public void setTestSetXml(File testSetXml) {
        this.testSetXml = testSetXml;
    }

    public File getJmeterCsv() {
        return jmeterCsv;
    }

    public void setJmeterCsv(File jmeterCsv) {
        this.jmeterCsv = jmeterCsv;
    }

    private boolean isNotNullAndReadable(File file) {
        return !(file == null) && isReadableFile(file);
    }

    private boolean isReadableFile(File file) {
        return file.canRead();
    }
}
