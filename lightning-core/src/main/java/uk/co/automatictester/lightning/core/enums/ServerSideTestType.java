package uk.co.automatictester.lightning.core.enums;

public enum ServerSideTestType {
    LESS_THAN("Less than"),
    BETWEEN("Between"),
    GREATER_THAN("Greater than");

    private String value;

    private ServerSideTestType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
