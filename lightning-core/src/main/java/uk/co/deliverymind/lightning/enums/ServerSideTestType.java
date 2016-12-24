package uk.co.deliverymind.lightning.enums;

public enum ServerSideTestType {
    LESS_THAN("Less than"),
    BETWEEN("Between"),
    GREATER_THAN("Greater than");

    private final String value;

    private ServerSideTestType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
