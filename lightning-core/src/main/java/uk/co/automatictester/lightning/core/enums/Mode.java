package uk.co.automatictester.lightning.core.enums;

public enum Mode {
    verify("verify"),
    report("report");

    private String value;

    Mode(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
