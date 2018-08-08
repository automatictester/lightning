package uk.co.automatictester.lightning.lambda;

public class LightningRequestValidator {

    public static void validate(LightningRequest request) {
        String bucket = request.getBucket();
        String region = request.getRegion();
        String mode = request.getMode();
        String xml = request.getXml();
        String jmeterCsv = request.getJmeterCsv();

        throwExceptionOnNullBucket(bucket);
        throwExceptionOnNullRegion(region);
        throwExceptionOnNullMode(mode);
        throwExceptionOnInvalidMode(mode);
        throwExceptionOnMissingReportModeInputs(mode, jmeterCsv);
        throwExceptionOnMissingVerifyModeInputs(mode, xml, jmeterCsv);
    }

    private static void throwExceptionOnNullBucket(String bucket) {
        if (bucket == null) {
            throw new LightningRequestException("Request must specify 'bucket'");
        }
    }

    private static void throwExceptionOnNullRegion(String region) {
        if (region == null) {
            throw new LightningRequestException("Request must specify 'region'");
        }
    }

    private static void throwExceptionOnNullMode(String mode) {
        if (mode == null) {
            throw new LightningRequestException("Request must specify 'mode'");
        }
    }

    private static void throwExceptionOnInvalidMode(String mode) {
        if (!(mode.equals("report") || mode.equals("verify"))) {
            throw new LightningRequestException("Request must specify 'mode'. Allowed values: 'report' or 'verify'");
        }
    }

    private static void throwExceptionOnMissingReportModeInputs(String mode, String jmeterCsv) {
        if (mode.equals("report")) {
            if (jmeterCsv == null) {
                throw new LightningRequestException("Request must specify 'jmeterCsv'");
            }
        }
    }

    private static void throwExceptionOnMissingVerifyModeInputs(String mode, String xml, String jmeterCsv) {
        if (mode.equals("verify")) {
            if (xml == null) {
                throw new LightningRequestException("Request must specify 'xml'");
            } else if (jmeterCsv == null) {
                throw new LightningRequestException("Request must specify 'jmeterCsv'");
            }
        }
    }
}
