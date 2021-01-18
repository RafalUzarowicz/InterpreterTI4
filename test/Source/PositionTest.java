package Source;

import Utilities.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 */
class PositionTest {
    @Test
    void testClone() throws Exception {
        // Arrange
        Position p1 = new Position();
        Position p2 = (Position) p1.clone();
        // Act
        p2.advanceLine();
        p2.advanceColumn();
        // Assert
        assertNotEquals(p1.toString(), p2.toString());
    }

    @Test
    void testAdvanceLine() {
        // Arrange
        Position p1 = new Position();
        // Act
        p1.advanceColumn();
        p1.advanceColumn();
        p1.advanceColumn();
        // Assert
        assertEquals(p1.toString(), "<" + 1 + " : " + 4 + ">");
    }

    @Test
    void testAdvanceColumn() {
        // Arrange
        Position p1 = new Position();
        // Act
        p1.advanceLine();
        p1.advanceLine();
        p1.advanceLine();
        // Assert
        assertEquals(p1.toString(), "<" + 4 + " : " + 1 + ">");
    }

    @Test
    void testResetColumn() {
        // Arrange
        Position p1 = new Position();
        // Act
        p1.advanceColumn();
        p1.advanceColumn();
        p1.advanceColumn();
        p1.resetColumn();
        // Assert
        assertEquals(p1.toString(), "<" + 1 + " : " + 1 + ">");
    }
}