package uk.co.automatictester.lightning.enums;

public enum JMeterColumns {
    TRANSACTION_LABEL_INDEX(0),
    TRANSACTION_DURATION_INDEX(1),
    TRANSACTION_RESULT_INDEX(2),
    TRANSACTION_TIMESTAMP(3),
    MAX_NUMBER_OF_LONGEST_TRANSACTIONS(5);

    private int value;

    private JMeterColumns(int value) {
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