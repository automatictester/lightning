package uk.co.automatictester.lightning.enums;

public enum TestResult {
    PASS("Pass"),
    FAIL("FAIL"),
    IGNORED("IGNORED");

    private final String value;

    private TestResult(String value) {
        this.value = value;
    }

    public String toString() {
        return this.value;
    }
}
