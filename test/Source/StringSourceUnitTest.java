package Source;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringSourceUnitTest {

    @Test
    void get() {
        StringSource source = new StringSource("tak");
        assertEquals(source.get(), 't');
        assertEquals(source.get(), 'a');
        assertEquals(source.get(), 'k');
        assertEquals(source.get(), -1);
    }

    @Test
    void peek() {
        StringSource source = new StringSource("tak");
        assertEquals(source.peek(), 't');
        source.get();
        assertEquals(source.peek(), 'a');
        source.get();
        assertEquals(source.peek(), 'k');
        source.get();
        assertEquals(source.get(), -1);
    }

    @Test
    void next() {
        StringSource source = new StringSource("tak");
        assertEquals(source.peek(), 't');
        source.next();
        assertEquals(source.peek(), 'a');
        source.next();
        assertEquals(source.peek(), 'k');
        source.next();
        assertEquals(source.get(), -1);
    }

    @Test
    void getPosition() {
        StringSource source = new StringSource("tak");
        Position position = new Position();
        assertEquals(source.getPosition().toString(), position.toString());
        source.next();
        position.advanceColumn();
        assertEquals(source.getPosition().toString(), position.toString());
        source.next();
        position.advanceColumn();
        assertEquals(source.getPosition().toString(), position.toString());
        source.next();
        assertEquals(source.get(), -1);
    }
}