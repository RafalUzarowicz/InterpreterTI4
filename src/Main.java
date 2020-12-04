import Scanner.Scanner;
import Source.ResourceFileSource;

public class Main {
    public static void main(String[] args){
        ResourceFileSource resourceFileSource = new ResourceFileSource("first.twlan");
        Scanner scanner = new Scanner(resourceFileSource);
        scanner.next();
        System.out.println(scanner.get());

//        int c = 0;
//        System.out.println(resourceFileSource.getPosition());
//        while((c=resourceFileSource.get())!= -1){
//            System.out.println(String.format("0x%02X", c)+ " "+ (char) c);
//            System.out.println(resourceFileSource.getPosition());
//        }
    }
}
