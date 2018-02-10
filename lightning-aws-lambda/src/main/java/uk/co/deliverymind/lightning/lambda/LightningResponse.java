package uk.co.deliverymind.lightning.lambda;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LightningResponse {

    private int exitCode;
    private String junitReport;
    private String jenkinsReport;
    private String teamCityReport;
    private String jmeterReport;
    private String combinedTestReport;
}
