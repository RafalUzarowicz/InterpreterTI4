package Source;

import java.io.*;
import java.util.Objects;
/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 *
 * Source implementation that searches file in resources.
 */
public class ResourceFileSource implements ISource {
    private final Position position;
    private final BufferedReader bufferedReader;
    private int character;

    public ResourceFileSource(String fileName) throws Exception {
        position = new Position();

        try {
            ClassLoader classLoader = getClass().getClassLoader();
            String filePath = Objects.requireNonNull(classLoader.getResource(fileName)).getFile();
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
