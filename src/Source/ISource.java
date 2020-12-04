package Source;

/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 *
 * Source interface
 */
public interface ISource {
    // Look at value without moving to next one.
    int peek();
    // Get value and move to the next one.
    int get();
    // Move to the next value.
    void next();
    // Get current position of cursor.
    Position getPosition();
}
