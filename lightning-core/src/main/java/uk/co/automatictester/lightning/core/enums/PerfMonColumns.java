package uk.co.automatictester.lightning.core.enums;

public enum PerfMonColumns {
    TIMESTAMP(0),
    VALUE(1),
    HOST_AND_METRIC(2);

    private int column;

    PerfMonColumns(int column) {
        this.column = column;
    }

    public int getColumn() {
        return column;
    }
}
