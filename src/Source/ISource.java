package Source;

public interface ISource {
    int peek();
    int get();
    void next();
    Position getPosition();
}
