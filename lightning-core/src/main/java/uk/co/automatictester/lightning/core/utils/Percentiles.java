package uk.co.automatictester.lightning.core.utils;

public class Percentiles {

    private Percentiles() {
    }

    public static boolean isPercentile(int percentile) {
        return (percentile > 0) && (percentile <= 100);
    }
}
