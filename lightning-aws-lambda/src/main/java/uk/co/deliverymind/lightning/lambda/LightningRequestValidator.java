package uk.co.deliverymind.lightning.lambda;

public class LightningRequestValidator {

    public static void validate(LightningRequest request) {
        String bucket = request.getBucket();
        String region = request.getRegion();
        String mode = request.getMode();
        String xml = request.getXml();
        String jmeterCsv = request.getJmeterCsv();

        if (bucket == null) {
            throw new LightningRequestException("Request must specify 'bucket'");
        } else if (region == null) {
            throw new LightningRequestException("Request must specify 'region'");
        } else if (mode == null) {
            throw new LightningRequestException("Request must specify 'mode'");
        } else if (!(mode.equals("report") || mode.equals("verify"))) {
            throw new LightningRequestException("Request must specify 'mode'. Allowed values: 'report' or 'verify'");
        } else if (mode.equals("report")) {
            if (jmeterCsv == null) {
                throw new LightningRequestException("Request must specify 'jmeterCsv'");
            }
        } else if (mode.equals("verify")) {
            if (xml == null) {
                throw new LightningRequestException("Request must specify 'xml'");
            } else if (jmeterCsv == null) {
                throw new LightningRequestException("Request must specify 'jmeterCsv'");
            }
        }
    }
}
