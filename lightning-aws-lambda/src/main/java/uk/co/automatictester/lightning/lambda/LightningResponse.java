package uk.co.automatictester.lightning.lambda;

public class LightningResponse {

    private int exitCode = 0;
    private String junitReport;
    private String jenkinsReport;
    private String teamCityReport;
    private String jmeterReport;
    private String combinedTestReport;

    public LightningResponse() {
    }

    public int getExitCode() {
        return exitCode;
    }

    public void setExitCode(int exitCode) {
        this.exitCode = exitCode;
    }

    public String getJunitReport() {
        return junitReport;
    }

    public void setJunitReport(String junitReport) {
        this.junitReport = junitReport;
    }

    public String getJenkinsReport() {
        return jenkinsReport;
    }

    public void setJenkinsReport(String jenkinsReport) {
        this.jenkinsReport = jenkinsReport;
    }

    public String getTeamCityReport() {
        return teamCityReport;
    }

    public void setTeamCityReport(String teamCityReport) {
        this.teamCityReport = teamCityReport;
    }

    public String getJmeterReport() {
        return jmeterReport;
    }

    public void setJmeterReport(String jmeterReport) {
        this.jmeterReport = jmeterReport;
    }

    public String getCombinedTestReport() {
        return combinedTestReport;
    }

    public void setCombinedTestReport(String combinedTestReport) {
        this.combinedTestReport = combinedTestReport;
    }
}
