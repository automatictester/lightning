package uk.co.automatictester.lightning.lambda;

public class LightningRequest {

    private String bucket;
    private String region;
    private String mode;
    private String xml;
    private String jmeterCsv;
    private String perfmonCsv;

    public LightningRequest() {
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public String getJmeterCsv() {
        return jmeterCsv;
    }

    public void setJmeterCsv(String jmeterCsv) {
        this.jmeterCsv = jmeterCsv;
    }

    public String getPerfmonCsv() {
        return perfmonCsv;
    }

    public void setPerfmonCsv(String perfmonCsv) {
        this.perfmonCsv = perfmonCsv;
    }
}
