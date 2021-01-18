package Source;

import Utilities.Constants;
import Utilities.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 */
class StringSourceTest {

    @Test
    void get() {
        // Arrange
        StringSource source = new StringSource("tak");
        // Act
        // Assert
        assertEquals(source.get(), 't');
        assertEquals(source.get(), 'a');
        assertEquals(source.get(), 'k');
        assertEquals(source.get(), Constants.Source.EOF);
    }

    @Test
    void peek() {
        // Arrange
        StringSource source = new StringSource("tak");
        // Act + Assert
        assertEquals(source.peek(), 't');
        source.get();
        assertEquals(source.peek(), 'a');
        source.get();
        assertEquals(source.peek(), 'k');
        source.get();
        assertEquals(source.get(), Constants.Source.EOF);
    }

    @Test
    void next() {
        // Arrange
        StringSource source = new StringSource("tak");
        // Act + Assert
        assertEquals(source.peek(), 't');
        source.next();
        assertEquals(source.peek(), 'a');
        source.next();
        assertEquals(source.peek(), 'k');
        source.next();
        assertEquals(source.get(), Constants.Source.EOF);
    }

    @Test
    void getPosition() {
        // Arrange
        StringSource source = new StringSource("tak");
        Position position = new Position();
        // Act + Assert
        assertEquals(source.getPosition().toString(), position.toString());
        source.next();
        position.advanceColumn();
        assertEquals(source.getPosition().toString(), position.toString());
        source.next();
        position.advanceColumn();
        assertEquals(source.getPosition().toString(), position.toString());
        source.next();
        assertEquals(source.get(), Constants.Source.EOF);
    }
}