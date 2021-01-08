package Source;

import java.io.*;

/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 * <p>
 * Source implementation that uses path to file.
 */
public class FileSource implements ISource {
    private final Position position;
    private final BufferedReader bufferedReader;
    private int character;

    public FileSource(String filePath) throws Exception {
        position = new Position();
        File file = new File(filePath);
        FileReader fileReader = new FileReader(file);
        bufferedReader = new BufferedReader(fileReader);
        character = bufferedReader.read();
    }

    @Override
    public int get() throws Exception {
        int tempChar = character;
        next();
        return tempChar;
    }

    @Override
    public int peek() {
        return character;
    }

    @Override
    public void next() throws Exception {
        character = bufferedReader.read();
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
