package uk.co.automatictester.lightning.core.enums;

public enum JmeterColumns {
    TRANSACTION_LABEL(0),
    TRANSACTION_DURATION(1),
    TRANSACTION_RESULT(2),
    TRANSACTION_TIMESTAMP(3);

    private int column;

    JmeterColumns(int column) {
        this.column = column;
    }

    public int getColumn() {
        return column;
    }
}
