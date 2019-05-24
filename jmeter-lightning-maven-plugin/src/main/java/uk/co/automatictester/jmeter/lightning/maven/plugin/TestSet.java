package uk.co.automatictester.jmeter.lightning.maven.plugin;

import org.apache.maven.plugin.MojoExecutionException;

import java.io.File;

public class TestSet {

  private File testSetXml;
  private File jmeterCsv;
  private File perfmonCsv;
  private String junitReportSuffix;

  File getTestSetXml() {
    return testSetXml;
  }

  File getJmeterCsv() throws MojoExecutionException {
    if (jmeterCsv == null)
      throw new MojoExecutionException("jmeterCsv is a required parameter");
    return jmeterCsv;
  }

  File getPerfmonCsv() {
    return perfmonCsv;
  }

  String getJunitReportSuffix() {
    return junitReportSuffix;
  }
}