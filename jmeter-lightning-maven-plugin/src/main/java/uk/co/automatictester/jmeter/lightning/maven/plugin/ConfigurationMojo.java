package uk.co.automatictester.jmeter.lightning.maven.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;
import uk.co.automatictester.lightning.core.enums.Mode;

import java.io.File;

abstract class ConfigurationMojo extends AbstractMojo {

    /**
     * Execution mode.
     * Allowed values: verify, report
     */
    @Parameter(property = "lightning.mode")
    Mode mode;

    /**
     * Lightning XML config file with test definitions
     */
    @Parameter(property = "lightning.testSetXml")
    File testSetXml;

    /**
     * JMeter CSV file
     */
    @Parameter(required = true, property = "lightning.jmeterCsv")
    File jmeterCsv;

    /**
     * PerfMon CSV file
     */
    @Parameter(property = "lightning.perfmonCsv")
    File perfmonCsv;

    /**
     * JUnit report suffix
     */
    @Parameter(property = "lightning.junitReportSuffix")
    String junitReportSuffix;

    /**
     * Whether or not subsequent tests should be executed on any failure.
     */
    @Parameter(property = "lightning.continueOnFailure")
    boolean continueOnFailure;
}
