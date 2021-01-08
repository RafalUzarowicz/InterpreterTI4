package Source;

/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 * <p>
 * Simple class to represent indexes of position.
 */
public class Pair implements Cloneable {
    private int first;
    private int second;

    public Pair(int first, int second) {
        super();
        this.first = first;
        this.second = second;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public boolean equals(Object other) {
        if (other instanceof Pair) {
            Pair otherPair = (Pair) other;
            return this.first == otherPair.first && this.second == otherPair.second;
        }
        return false;
    }

    public String toString() {
        return "<" + first + " : " + second + ">";
    }

    public int getFirst() {
        return first;
    }

    public void setFirst(int first) {
        this.first = first;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }
}
