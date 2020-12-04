package Source;

import java.io.*;

public class ResourceFileSource implements ISource {
    private Position position;
    private File file;
    private FileReader fileReader;
    private BufferedReader bufferedReader;
    private int character;

    public ResourceFileSource(String filePath){
        position = new Position();

        try {
            ClassLoader classLoader = getClass().getClassLoader();
            file = new File(classLoader.getResource(filePath).getFile());
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);
            character = bufferedReader.read();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("I/O error.");
            e.printStackTrace();
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
        switch (character){
            case '\n':
                position.advanceLine();
                position.resetColumn();
                position.advanceColumn(-1);
                break;
            default:
                position.advanceColumn();
                break;
        }
    }

    @Override
    public Position getPosition() {
        return position;
    }
}
