package uk.co.automatictester.lightning.core.structures;

import uk.co.automatictester.lightning.core.tests.base.LightningTest;

import java.util.ArrayList;
import java.util.List;

public class LightningTests {

    private static LightningTests instance;
    private List<LightningTest> tests = new ArrayList<>();

    private LightningTests() {
    }

    public synchronized static LightningTests getInstance() {
        if (instance == null) {
            instance = new LightningTests();
        }
        return instance;
    }

    public void flush() {
        tests.clear();
    }

    public void add(LightningTest test) {
        tests.add(test);
    }

    public void addAll(List<LightningTest> test) {
        tests.addAll(test);
    }

    public List<LightningTest> get() {
        return tests;
    }

    public int size() {
        return tests.size();
    }
}
