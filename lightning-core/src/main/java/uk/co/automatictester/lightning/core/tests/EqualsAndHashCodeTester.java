package uk.co.automatictester.lightning.core.tests;

public class EqualsAndHashCodeTester<T, U> {

    // TODO: add meaningful messages in the event of failure, i.e. bring asserts inside to EqualsAndHashCodeTester
    // TODO: 100% unit test coverage
    // TODO: test method should throw an exception if not all test data is provided

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
        return isReflexive() &&
                isSymmetric() &&
                isTransitive() &&
                isNotEqualNull() &&
                isNotEqualDifferentInstance() &&
                isNotEqualDifferentClass() &&
                isConsistent() &&
                isHashCodeConstant() &&
                isHashCodeSameForEqualObject();
    }

    private boolean isReflexive() {
        return instanceT1.equals(instanceT1);
    }

    private boolean isSymmetric() {
        return instanceT1.equals(instanceT2) == instanceT2.equals(instanceT1);
    }

    private boolean isTransitive() {
        return instanceT1.equals(instanceT2) && instanceT2.equals(instanceT3) && instanceT3.equals(instanceT1);
    }

    private boolean isNotEqualNull() {
        return !instanceT1.equals(null);
    }

    private boolean isNotEqualDifferentInstance() {
        return !instanceT1.equals(instanceTX);
    }

    private boolean isNotEqualDifferentClass() {
        return !instanceT1.equals(instanceU);
    }

    private boolean isConsistent() {
        boolean isConsisent = true;
        for (int i = 0; i < 10; i++) {
            boolean isTranstivie = isTransitive() && isNotEqualDifferentInstance();
            if (!isTranstivie) {
                isConsisent = false;
            }
        }
        return isConsisent;
    }

    private boolean isHashCodeConstant() {
        return instanceT1.hashCode() == instanceT1.hashCode();
    }

    private boolean isHashCodeSameForEqualObject() {
        return instanceT1.hashCode() == instanceT2.hashCode();
    }
}
