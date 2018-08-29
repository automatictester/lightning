package uk.co.automatictester.lightning.core.structures;

import uk.co.automatictester.lightning.core.tests.base.LightningTest;

import java.util.ArrayList;
import java.util.List;

public class LightningTests {

    private static List<LightningTest> tests = new ArrayList<>();

    private LightningTests() {
    }

    public static void flush() {
        tests.clear();
    }

    public static void add(LightningTest test) {
        tests.add(test);
    }

    public static void addAll(List<LightningTest> test) {
        tests.addAll(test);
    }

    public static List<LightningTest> getTests() {
        return tests;
    }

    public static int size() {
        return tests.size();
    }
}
