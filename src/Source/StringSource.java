package Source;

public class StringSource implements ISource {
    private final Position position;
    private int character;
    private final String text;
    private int index;

    public StringSource(String string){
        this.text = string;
        position = new Position();
        index = 0;
        character = text.charAt(index);
    }

    @Override
    public int get() {
        int tempChar = character;
        next();
        return tempChar;
    }

    @Override
    public int peek() {
        return character;
    }

    @Override
    public void next() {
        try{
            character = text.charAt(++index);
        } catch (StringIndexOutOfBoundsException exception ){
            // TODO: lepsza obsluga wyjatkow
            character = -1;
        }
        if (character == '\n') {
            position.advanceLine();
            position.resetColumn();
            position.advanceColumn(-1);
        } else {
            position.advanceColumn();
        }
    }

    @Override
    public Position getPosition() {
        return position;
    }
}
