package uk.co.automatictester.lightning.enums;

public enum TestResult {
    PASS("Pass"),
    FAIL("FAIL"),
    ERROR("ERROR");

    private String value;

    private TestResult(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
