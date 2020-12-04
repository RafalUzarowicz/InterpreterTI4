package Source;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PositionUnitTest {
    @Test
    void testClone() throws Exception {
        Position p1 = new Position();
        Position p2 = (Position)p1.clone();
        p2.advanceLine();
        p2.advanceColumn();
        assertNotEquals(p1.toString(), p2.toString());
    }

    @Test
    void testAdvanceLine() {
        Position p1 = new Position();
        p1.advanceColumn();
        p1.advanceColumn();
        p1.advanceColumn();
        assertEquals(p1.toString(), "<" + 1 + " : " + 4 + ">");
    }

    @Test
    void testAdvanceColumn() {
        Position p1 = new Position();
        p1.advanceLine();
        p1.advanceLine();
        p1.advanceLine();
        assertEquals(p1.toString(), "<" + 4 + " : " + 1 + ">");
    }

    @Test
    void testResetColumn() {
        Position p1 = new Position();
        p1.advanceColumn();
        p1.advanceColumn();
        p1.advanceColumn();
        p1.resetColumn();
        assertEquals(p1.toString(), "<" + 1 + " : " + 1 + ">");
    }
}