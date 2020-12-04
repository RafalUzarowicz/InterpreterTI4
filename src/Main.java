import Scanner.Scanner;
import Scanner.Token;
import Source.FileSource;
import Source.ResourceFileSource;
import Source.StringSource;

public class Main {
    public static void main(String[] args){
        try {
            FileSource source = new FileSource("code/simple.twlan");
//            StringSource source = new StringSource("int _main(){};\nvar x = 999999;");
//            ResourceFileSource source = new ResourceFileSource("first.twlan");
            Scanner scanner = new Scanner(source);
            while(scanner.get().getType() != Token.Type.EOF){
                scanner.next();
                System.out.println(scanner.get());
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
