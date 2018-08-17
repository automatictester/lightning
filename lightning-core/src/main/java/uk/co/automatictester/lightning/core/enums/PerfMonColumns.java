package uk.co.automatictester.lightning.core.enums;

public enum PerfMonColumns {
    TIMESTAMP_INDEX(0),
    VALUE_INDEX(1),
    HOST_AND_METRIC_INDEX(2);

    private int value;

    private PerfMonColumns(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
