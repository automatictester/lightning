package uk.co.deliverymind.lightning.enums;

public enum TestResult {
    PASS("Pass"),
    FAIL("FAIL"),
    ERROR("ERROR");

    private final String value;

    private TestResult(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
