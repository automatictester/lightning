package uk.co.automatictester.lightning.core.utils;

public class Ordinals {

    /*
     * This could be a static member class of RespTimeNthPercentileTest class.
     * However, it is easier to test if a separate class on its own.
     */

    private static final String[] SUFIXES = new String[]{"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th"};

    private Ordinals() {
    }

    public static String fromInt(int i) {
        if ((i % 100) == 11 || (i % 100) == 12 || (i % 100) == 13) {
            return i + "th";
        } else {
            return i + SUFIXES[i % 10];
        }
    }
}
