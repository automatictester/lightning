package uk.co.automatictester.lightning.core.state.tests;

import uk.co.automatictester.lightning.core.tests.base.AbstractTest;

import java.util.ArrayList;
import java.util.List;

public class LightningTestSet {
    private final List<AbstractTest> tests = new ArrayList<>();

    public void flush() {
        tests.clear();
    }

    public void add(AbstractTest test) {
        tests.add(test);
    }

    public void addAll(List<AbstractTest> test) {
        tests.addAll(test);
    }

    public List<AbstractTest> get() {
        return tests;
    }

    public int size() {
        return tests.size();
    }
}
