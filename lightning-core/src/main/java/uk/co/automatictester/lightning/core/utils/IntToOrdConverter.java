package uk.co.automatictester.lightning.core.utils;

public class IntToOrdConverter {

    private IntToOrdConverter() {
    }

    public static String convert(int i) {
        String[] sufixes = new String[]{"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th"};
        if ((i % 100) == 11 || (i % 100) == 12 || (i % 100) == 13) {
            return i + "th";
        } else {
            return i + sufixes[i % 10];
        }
    }
}
