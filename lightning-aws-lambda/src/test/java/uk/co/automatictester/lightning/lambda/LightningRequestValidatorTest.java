package uk.co.automatictester.lightning.lambda;

import org.testng.annotations.Test;

public class LightningRequestValidatorTest {

    @Test
    public void testReport() {
        LightningRequest request = new LightningRequest();
        request.setBucket("bucket");
        request.setRegion("region");
        request.setMode("report");
        request.setJmeterCsv("jmeterCsv");
        LightningRequestValidator.validate(request);
    }

    @Test
    public void testVerify() {
        LightningRequest request = new LightningRequest();
        request.setBucket("bucket");
        request.setRegion("region");
        request.setMode("verify");
        request.setJmeterCsv("jmeterCsv");
        request.setXml("xml");
        LightningRequestValidator.validate(request);
    }

    @Test(expectedExceptions = LightningRequestException.class)
    public void testNoRegion() {
        LightningRequest request = new LightningRequest();
        request.setBucket("bucket");
        request.setMode("verify");
        request.setJmeterCsv("jmeterCsv");
        request.setXml("xml");
        LightningRequestValidator.validate(request);
    }

    @Test(expectedExceptions = LightningRequestException.class)
    public void testNoBucket() {
        LightningRequest request = new LightningRequest();
        request.setRegion("region");
        request.setMode("verify");
        request.setJmeterCsv("jmeterCsv");
        request.setXml("xml");
        LightningRequestValidator.validate(request);
    }

    @Test(expectedExceptions = LightningRequestException.class)
    public void testNoMode() {
        LightningRequest request = new LightningRequest();
        request.setBucket("bucket");
        request.setRegion("region");
        request.setJmeterCsv("jmeterCsv");
        request.setXml("xml");
        LightningRequestValidator.validate(request);
    }

    @Test(expectedExceptions = LightningRequestException.class)
    public void testIncorrectMode() {
        LightningRequest request = new LightningRequest();
        request.setBucket("bucket");
        request.setRegion("region");
        request.setMode("wrong");
        request.setJmeterCsv("jmeterCsv");
        request.setXml("xml");
        LightningRequestValidator.validate(request);
    }

    @Test(expectedExceptions = LightningRequestException.class)
    public void testNoJmeterCsvForReport() {
        LightningRequest request = new LightningRequest();
        request.setBucket("bucket");
        request.setRegion("region");
        request.setMode("report");
        LightningRequestValidator.validate(request);
    }

    @Test(expectedExceptions = LightningRequestException.class)
    public void testNoJmeterCsvForVerify() {
        LightningRequest request = new LightningRequest();
        request.setBucket("bucket");
        request.setRegion("region");
        request.setMode("verify");
        request.setXml("xml");
        LightningRequestValidator.validate(request);
    }

    @Test(expectedExceptions = LightningRequestException.class)
    public void testNoXmlForVerify() {
        LightningRequest request = new LightningRequest();
        request.setBucket("bucket");
        request.setRegion("region");
        request.setMode("verify");
        LightningRequestValidator.validate(request);
    }

}
