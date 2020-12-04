package Source;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PairUnitTest {

    @Test
    void testClone() {
        Pair p1 = new Pair(1,1);
        Pair p2 = null;
        try {
            p2 = (Pair)p1.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        if (p2 != null) {
            p2.setFirst(2);
            p2.setSecond(2);
            assertNotEquals(p1.getFirst(), p2.getFirst());
            assertNotEquals(p1.getSecond(), p2.getSecond());
        }
    }

    @Test
    void testEquals() {
        Pair p1 = new Pair(1,1);
        Pair p2 = new Pair(1,1);
        Pair p3 = new Pair(2,2);

        assertEquals(p1, p2);
        assertNotEquals(p1, p3);
    }
}