package Source;

public class Position {
    public Pair<Integer, Integer> getPosition() {
        return position;
    }

    private Pair<Integer, Integer> position;

    public Position(){
        this(0,0);
    }

    public Position(int line, int column){
        position = new Pair<>(line, column);
    }

    public void advanceLine(){
        advanceLine(1);
    }
    public void advanceLine(int amount){
        position.setFirst(position.getFirst()+amount);
    }
    public void advanceColumn(){
        advanceColumn(1);
    }
    public void advanceColumn(int amount){
        position.setSecond(position.getSecond()+amount);
    }

    public void resetColumn(){
        position.setSecond(0);
    }

    public String toString()
    {
        return position.toString();
    }
}
