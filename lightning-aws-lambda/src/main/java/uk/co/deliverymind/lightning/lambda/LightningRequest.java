package uk.co.deliverymind.lightning.lambda;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LightningRequest {

    private String bucket;
    private String region;

    private String mode;
    private String xml;
    private String jmeterCsv;
    private String perfmonCsv;
}
