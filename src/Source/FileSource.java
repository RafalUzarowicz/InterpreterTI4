package Source;

import java.io.*;
/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 *
 * Source implementation that uses path to file.
 */
public class FileSource implements ISource {
    private final Position position;
    private final BufferedReader bufferedReader;
    private int character;

    public FileSource(String filePath) throws Exception {
        position = new Position();

        try {
            File file = new File(filePath);
            FileReader fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);
            character = bufferedReader.read();
        } catch (FileNotFoundException e) {
            // TODO: lepsza obsluga wyjatkow
            throw new Exception("File not found.");
        } catch (IOException e) {
            // TODO: lepsza obsluga wyjatkow
            throw new Exception("I/O error.");
        }

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
            character = bufferedReader.read();
        } catch (IOException e) {
            System.out.println("I/O error.");
            e.printStackTrace();
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
