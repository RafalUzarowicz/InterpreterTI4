package Source;

import Utilities.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 */
class PairTest {

    @Test
    void testClone() throws CloneNotSupportedException {
        // Arrange
        Pair p1 = new Pair(1, 1);
        Pair p2;
        // Act
        p2 = (Pair) p1.clone();
        p2.setFirst(2);
        p2.setSecond(2);
        // Assert
        assertNotEquals(p1.getFirst(), p2.getFirst());
        assertNotEquals(p1.getSecond(), p2.getSecond());
    }

    @Test
    void testEquals() {
        // Arrange
        // Act
        Pair p1 = new Pair(1, 1);
        Pair p2 = new Pair(1, 1);
        Pair p3 = new Pair(2, 2);
        // Assert
        assertEquals(p1, p2);
        assertNotEquals(p1, p3);
    }
}