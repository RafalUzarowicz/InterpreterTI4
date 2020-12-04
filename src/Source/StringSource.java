package Source;

import Scanner.Constants;

/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 * <p>
 * Source implementation that uses single string as a source.
 */
public class StringSource implements ISource {
    private final Position position;
    private int character;
    private final String text;
    private int index;

    public StringSource(String string) {
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
        if (++index < text.length()) {
            character = text.charAt(index);
        } else {
            index = text.length();
            character = Constants.Source.EOF;
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
