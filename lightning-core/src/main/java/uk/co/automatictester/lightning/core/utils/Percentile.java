package uk.co.automatictester.lightning.core.utils;

public class Percentile {

    private Percentile() {
    }

    public static boolean isPercentile(int percentile) {
        return (percentile > 0) && (percentile <= 100);
    }
}
