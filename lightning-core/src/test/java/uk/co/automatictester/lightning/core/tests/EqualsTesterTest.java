package uk.co.automatictester.lightning.core.tests;

import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class EqualsTesterTest {

    class A {
        int a;

        public A(int a) {
            this.a = a;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj instanceof A) {
                A other = (A) obj;
                return this.a == other.a;
            }
            return false;
        }
    }

    class B {
        int b;

        public B(int b) {
            this.b = b;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj instanceof B) {
                B other = (B) obj;
                return this.b == other.b;
            }
            return false;
        }
    }

    @Test
    public void test() {
        A instanceA1 = new A(1);
        A instanceA2 = new A(1);
        A instanceA3 = new A(1);
        A instanceAX = new A(2);
        B instanceBX = new B(1);

        EqualsTester<A, B> tester = new EqualsTester<>();
        tester.addEqualObjects(instanceA1, instanceA2, instanceA3);
        tester.addNonEqualObject(instanceAX);
        tester.addNotInstanceof(instanceBX);
        assertThat(tester.test(), is(true));
    }
}
