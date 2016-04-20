package uk.co.automatictester.lightning.enums;

public enum Mode {
    VERIFY("verify"),
    REPORT("report");

    private final String value;

    private Mode(String value) {
        this.value = value;
    }

    public String toString() {
        return this.value;
    }
}
