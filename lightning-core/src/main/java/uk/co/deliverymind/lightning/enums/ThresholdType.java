package uk.co.deliverymind.lightning.enums;

public enum ThresholdType {
    NUMBER("Number"),
    PERCENT("Percent");

    private final String value;

    private ThresholdType(String value) {
        this.value = value;
    }

    public String toString() {
        return this.value;
    }
}
