package uk.co.automatictester.lightning.enums;

public enum ServerSideTestType {
    LESS_THAN("Less than"),
    BETWEEN("Between"),
    GREATER_THAN("Greater than");

    private final String value;

    private ServerSideTestType(String value) {
        this.value = value;
    }

    public String toString() {
        return this.value;
    }
}
