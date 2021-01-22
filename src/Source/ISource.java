package Source;

import Utilities.Position;

/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 * <p>
 * Source interface
 */
public interface ISource {
    // Look at value without moving to next one.
    int peek();

    // Get value and move to the next one.
    int get() throws Exception;

    // Move to the next value.
    void next() throws Exception;

    // Get current position of cursor.
    Position getPosition();
}
