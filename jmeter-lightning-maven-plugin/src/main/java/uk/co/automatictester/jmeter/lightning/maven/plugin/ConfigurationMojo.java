package uk.co.automatictester.jmeter.lightning.maven.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;
import uk.co.automatictester.lightning.core.enums.Mode;

import java.util.List;

abstract class ConfigurationMojo extends AbstractMojo {

    /**
     * Execution mode.
     * Allowed values: verify, report
     */
    @Parameter
    Mode mode;

    /**
     * A list of TestSets to be executed, containing properties:
     * testSetXml,
     * jmeterCsv (required),
     * perfmonCsv,
     * junitReportSuffix
     */
    @Parameter(required = true)
    List<TestSet> testSets;
}
