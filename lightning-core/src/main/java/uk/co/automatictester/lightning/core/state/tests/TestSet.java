package uk.co.automatictester.lightning.core.state.tests;

import uk.co.automatictester.lightning.core.tests.LightningTest;

import java.util.ArrayList;
import java.util.List;

import static uk.co.automatictester.lightning.core.enums.TestResult.*;

public class TestSet {

    private static final String TEAMCITY_STATISTICS = "##teamcity[buildStatisticValue key='%s' value='%s']%n";
    private final List<LightningTest> tests = new ArrayList<>();
    private final TestSetResults results = new TestSetResults();

    public void executeTests() {
        StringBuilder output = new StringBuilder();
        tests.forEach(test -> {
            test.execute();
            String testExecutionReport = test.getTestExecutionReport();
            output.append(testExecutionReport).append(System.lineSeparator());
        });
        String testExecutionReport = output.toString();
        results.setTestExecutionReport(testExecutionReport);
    }

    public void add(LightningTest test) {
        tests.add(test);
    }

    public void addAll(List<LightningTest> test) {
        tests.addAll(test);
    }

    public List<LightningTest> get() {
        return tests;
    }

    public int size() {
        return tests.size();
    }

    public String testExecutionReport() {
        return results.testExecutionReport();
    }

    public String testSetExecutionSummaryReport() {
        return results.testSetExecutionSummaryReport();
    }

    public String jenkinsSummaryReport() {
        return results.jenkinsSummary();
    }

    public String teamCityVerifyStatistics() {
        return results.teamCityVerifyStatistics();
    }

    public int failCount() {
        return results.failCount();
    }

    public int errorCount() {
        return results.errorCount();
    }

    public boolean hasFailed() {
        return results.hasFailed();
    }

    private class TestSetResults {
        private String testExecutionReport;

        void setTestExecutionReport(String testExecutionReport) {
            this.testExecutionReport = testExecutionReport;
        }

        String testExecutionReport() {
            return testExecutionReport;
        }

        boolean hasFailed() {
            return failCount() != 0 || errorCount() != 0;
        }

        String jenkinsSummary() {
            int executed = TestSet.this.size();
            int failed = TestSet.this.failCount() + TestSet.this.errorCount();
            return String.format("Tests executed: %s, failed: %s", executed, failed);
        }

        String testSetExecutionSummaryReport() {
            return String.format("%n============= EXECUTION SUMMARY =============%n"
                            + "Tests executed:    %s%n"
                            + "Tests passed:      %s%n"
                            + "Tests failed:      %s%n"
                            + "Tests errors:      %s%n"
                            + "Test set status:   %s",
                    TestSet.this.size(),
                    passCount(),
                    failCount(),
                    errorCount(),
                    testSetStatus());
        }

        public String teamCityVerifyStatistics() {
            StringBuilder output = new StringBuilder();
            TestSet.this.get().forEach(test -> {
                String teamCityConsoleOutputEntry = String.format(TEAMCITY_STATISTICS, test.name(), test.actualResult());
                output.append(teamCityConsoleOutputEntry);
            });
            return output.toString();
        }

        int failCount() {
            return (int) TestSet.this.get().stream()
                    .filter(t -> t.result().equals(FAIL))
                    .count();
        }

        int errorCount() {
            return (int) TestSet.this.get().stream()
                    .filter(t -> t.result().equals(ERROR))
                    .count();
        }

        private int passCount() {
            return (int) TestSet.this.get().stream()
                    .filter(t -> t.result().equals(PASS))
                    .count();
        }

        private String testSetStatus() {
            return hasFailed() ? FAIL.toString() : PASS.toString();
        }
    }
}
