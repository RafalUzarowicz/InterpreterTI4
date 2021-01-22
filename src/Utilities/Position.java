package Utilities;

/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 * <p>
 * Simple class that takes care of position in source.
 */
public class Position implements Cloneable {
    public Pair getPosition() {
        return position;
    }

    private Pair position;

    public Position() {
        this(Constants.Position.FIRST_INDEX, Constants.Position.FIRST_INDEX);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Position cloned = (Position) super.clone();
        cloned.position = (Pair) cloned.getPosition().clone();
        return cloned;
    }

    public Position(int line, int column) {
        position = new Pair(line, column);
    }

    public void advanceLine() {
        advanceLine(1);
    }

    public void advanceLine(int amount) {
        position.setFirst(position.getFirst() + amount);
    }

    public void advanceColumn() {
        advanceColumn(1);
    }

    public void advanceColumn(int amount) {
        position.setSecond(position.getSecond() + amount);
    }

    public void resetColumn() {
        position.setSecond(Constants.Position.FIRST_INDEX);
    }

    public int getLine() {
        return position.getFirst();
    }

    public String toString() {
        return position.toString();
    }
}
