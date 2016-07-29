package uk.co.automatictester.lightning.enums;

public enum Mode {
    verify("verify"),
    report("report");

    private final String value;

    private Mode(String value) {
        this.value = value;
    }

    public String toString() {
        return this.value;
    }
}
