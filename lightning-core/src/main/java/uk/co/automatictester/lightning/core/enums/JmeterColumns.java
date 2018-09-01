package uk.co.automatictester.lightning.core.enums;

public enum JmeterColumns {
    TRANSACTION_LABEL_INDEX(0),
    TRANSACTION_DURATION_INDEX(1),
    TRANSACTION_RESULT_INDEX(2),
    TRANSACTION_TIMESTAMP(3);

    private int value;

    JmeterColumns(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
