package uk.co.automatictester.lightning.core.tests;

public class EqualsTester<T, U> {

    // TODO: add meaningful messages in the event of failure, i.e. bring asserts inside to EqualsTester
    // TODO: 100% unit test coverage

    private T instanceT1;
    private T instanceT2;
    private T instanceT3;
    private T instanceTX;
    private U instanceU;

    public void addEqualObjects(T instanceA, T instanceB, T instanceC) {
        this.instanceT1 = instanceA;
        this.instanceT2 = instanceB;
        this.instanceT3 = instanceC;
    }

    public void addNonEqualObject(T instance) {
        this.instanceTX = instance;
    }

    public void addNotInstanceof(U instance) {
        this.instanceU = instance;
    }

    public boolean test() {
        return isReflexive() && isSymmetric() && isTransitive() && isNotNull() && isNotEqual() && isNotEqualNotInstanceof() && isConsistent();
    }

    private boolean isReflexive() {
        return instanceT1.equals(instanceT1) && instanceT2.equals(instanceT2) && instanceT3.equals(instanceT3);
    }

    private boolean isSymmetric() {
        return instanceT1.equals(instanceT2) == instanceT2.equals(instanceT1)
                && instanceT2.equals(instanceT3) == instanceT3.equals(instanceT2)
                && instanceT3.equals(instanceT1) == instanceT1.equals(instanceT3);
    }

    private boolean isTransitive() {
        return instanceT1.equals(instanceT2) && instanceT2.equals(instanceT3) && instanceT3.equals(instanceT1);
    }

    private boolean isConsistent() {
        boolean isConsisent = true;
        for (int i = 0; i < 10; i++) {
            boolean isTranstivie = isTransitive() && isNotEqual();
            if (!isTranstivie) {
                isConsisent = false;
            }
        }
        return isConsisent;
    }

    private boolean isNotNull() {
        return !instanceT1.equals(null) && !instanceT2.equals(null) && !instanceT3.equals(null);
    }

    private boolean isNotEqual() {
        return !instanceT1.equals(instanceTX) && !instanceT2.equals(instanceTX) && !instanceT3.equals(instanceTX);
    }

    private boolean isNotEqualNotInstanceof() {
        return !instanceT1.equals(instanceU) && !instanceT2.equals(instanceU) && !instanceT3.equals(instanceU) && !instanceTX.equals(instanceU);
    }
}
