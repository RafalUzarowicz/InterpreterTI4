package Source;

public class Pair<FirstType, SecondType>{
    private FirstType first;
    private SecondType second;

    public Pair(FirstType first, SecondType second) {
        super();
        this.first = first;
        this.second = second;
    }

    public boolean equals(Object other) {
        if (other instanceof Pair) {
            Pair otherPair = (Pair) other;
            return
                    ( this.first == otherPair.first ||
                            ( this.first != null && otherPair.first != null && this.first.equals(otherPair.first)))
                            &&
                            (  this.second == otherPair.second ||
                                    ( this.second != null && otherPair.second != null &&
                                            this.second.equals(otherPair.second)));
        }
        return false;
    }

    public String toString()
    {
        return "<" + first + " : " + second + ">";
    }

    public FirstType getFirst() {
        return first;
    }

    public void setFirst(FirstType first) {
        this.first = first;
    }

    public SecondType getSecond() {
        return second;
    }

    public void setSecond(SecondType second) {
        this.second = second;
    }
}
