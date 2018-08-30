package uk.co.automatictester.lightning.core.utils;

public class IntToOrdConverter {

    private static final String[] SUFIXES = new String[]{"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th"};

    private IntToOrdConverter() {
    }

    public static String convert(int i) {
        if ((i % 100) == 11 || (i % 100) == 12 || (i % 100) == 13) {
            return i + "th";
        } else {
            return i + SUFIXES[i % 10];
        }
    }
}
