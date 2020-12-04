package Scanner;

import Source.FileSource;
import Source.Position;
import Source.ResourceFileSource;
import Source.StringSource;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScannerTest {
    private void checkToken(Scanner scanner, Token token){
        try {
            scanner.next();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        Token t = scanner.get();
        assertEquals(token.getType(), t.getType());
        assertEquals(token.getPosition().toString(), t.getPosition().toString());
        assertEquals(token.getValue(), token.getValue());
    }

    @Test
    void simplest() {
        StringSource source = new StringSource("var x = 2;");
        Scanner scanner = new Scanner(source);

        checkToken(scanner, new Token(Token.Type.VarType, new Position(1,1), "var"));
        checkToken(scanner, new Token(Token.Type.Identifier, new Position(1,5), "x"));
        checkToken(scanner, new Token(Token.Type.Equals, new Position(1,7), "="));
        checkToken(scanner, new Token(Token.Type.NumberLiteral, new Position(1,9), "2"));
        checkToken(scanner, new Token(Token.Type.Semicolon, new Position(1,10), ";"));
    }

    @Test
    void simple() {
        /*
        int main( string s ){
            color c = Red;
            int x12 = h9;
        }
        */
        try{
            ResourceFileSource source = new ResourceFileSource("simple.twlan");

            Scanner scanner = new Scanner(source);

            checkToken(scanner, new Token(Token.Type.Type, new Position(1,1), "main"));
            checkToken(scanner, new Token(Token.Type.Identifier, new Position(1,5), "main"));
            checkToken(scanner, new Token(Token.Type.ParenthesisLeft, new Position(1,9), "("));
            checkToken(scanner, new Token(Token.Type.Type, new Position(1,11), "string"));
            checkToken(scanner, new Token(Token.Type.Identifier, new Position(1,18), "s"));
            checkToken(scanner, new Token(Token.Type.ParenthesisRight, new Position(1,20), ")"));
            checkToken(scanner, new Token(Token.Type.BracesLeft, new Position(1,21), "{"));
            checkToken(scanner, new Token(Token.Type.Type, new Position(2,5), "color"));
            checkToken(scanner, new Token(Token.Type.Identifier, new Position(2,11), "c"));
            checkToken(scanner, new Token(Token.Type.Equals, new Position(2,13), "="));
            checkToken(scanner, new Token(Token.Type.ColorLiteral, new Position(2,15), "Red"));
            checkToken(scanner, new Token(Token.Type.Semicolon, new Position(2,18), ";"));
            checkToken(scanner, new Token(Token.Type.Type, new Position(3,5), "int"));
            checkToken(scanner, new Token(Token.Type.Identifier, new Position(3,9), "x12"));
            checkToken(scanner, new Token(Token.Type.Equals, new Position(3,13), "="));
            checkToken(scanner, new Token(Token.Type.HexLiteral, new Position(3,15), "h9"));
            checkToken(scanner, new Token(Token.Type.Semicolon, new Position(3,17), ";"));
            checkToken(scanner, new Token(Token.Type.BracesRight, new Position(4,1), "}"));

        } catch (Exception exception) {
            // TODO: lepsza obsluga wyjatkow
            exception.printStackTrace();
        }
    }

    @Test
    void advanced() {
        /*
        int increment(int x){
            print("Before incrementation: ", x);
            return x+1;
        }

        int main(){
            color[] players = color[]{Red, Blue};
            if( player(Yellow) has command >= 1 ){
                foreach(var p:players){
                        print(p);
                }
            }else{
                print("Tak");
            }
            var[] units = unit[2];
            unit[0] = Fighter;
            unit[1] = Infantry;

            return 0;
        }
        */
        try{
            FileSource source = new FileSource("code/advance.twlan");

            Scanner scanner = new Scanner(source);

            checkToken(scanner, new Token(Token.Type.Type, new Position(1,1), "int"));
            checkToken(scanner, new Token(Token.Type.Identifier, new Position(1,5), "increment"));
            checkToken(scanner, new Token(Token.Type.ParenthesisLeft, new Position(1,14), "("));
            checkToken(scanner, new Token(Token.Type.Type, new Position(1,15), "int"));
            checkToken(scanner, new Token(Token.Type.Identifier, new Position(1,19), "x"));
            checkToken(scanner, new Token(Token.Type.ParenthesisRight, new Position(1,20), ")"));
            checkToken(scanner, new Token(Token.Type.BracesLeft, new Position(1,21), "{"));
            checkToken(scanner, new Token(Token.Type.Print, new Position(2,2), "print"));
            checkToken(scanner, new Token(Token.Type.ParenthesisLeft, new Position(2,7), "("));
            checkToken(scanner, new Token(Token.Type.StringLiteral, new Position(2,8), "\"Before incrementation: \""));
            checkToken(scanner, new Token(Token.Type.Comma, new Position(2,33), ","));
            checkToken(scanner, new Token(Token.Type.Identifier, new Position(2,35), "x"));
            checkToken(scanner, new Token(Token.Type.ParenthesisRight, new Position(2,36), ")"));
            checkToken(scanner, new Token(Token.Type.Semicolon, new Position(2,37), ";"));
            checkToken(scanner, new Token(Token.Type.Return, new Position(3,2), "return"));
            checkToken(scanner, new Token(Token.Type.Identifier, new Position(3,9), "x"));
            checkToken(scanner, new Token(Token.Type.Plus, new Position(3,10), "+"));
            checkToken(scanner, new Token(Token.Type.NumberLiteral, new Position(3,11), "1"));
            checkToken(scanner, new Token(Token.Type.Semicolon, new Position(3,12), ";"));
            checkToken(scanner, new Token(Token.Type.BracesRight, new Position(4,1), "}"));
            checkToken(scanner, new Token(Token.Type.Type, new Position(6,1), "int"));
            checkToken(scanner, new Token(Token.Type.Identifier, new Position(6,5), "main"));
            checkToken(scanner, new Token(Token.Type.ParenthesisLeft, new Position(6,9), "("));
            checkToken(scanner, new Token(Token.Type.ParenthesisRight, new Position(6,10), ")"));
            checkToken(scanner, new Token(Token.Type.BracesLeft, new Position(6,11), "{"));
            checkToken(scanner, new Token(Token.Type.Type, new Position(7,5), "color"));
            checkToken(scanner, new Token(Token.Type.BracketsLeft, new Position(7,10), "["));
            checkToken(scanner, new Token(Token.Type.BracketsRight, new Position(7,11), "]"));
            checkToken(scanner, new Token(Token.Type.Identifier, new Position(7,13), "players"));
            checkToken(scanner, new Token(Token.Type.Equals, new Position(7,21), "="));
            checkToken(scanner, new Token(Token.Type.Type, new Position(7,23), "color"));
            checkToken(scanner, new Token(Token.Type.BracketsLeft, new Position(7,28), "["));
            checkToken(scanner, new Token(Token.Type.BracketsRight, new Position(7,29), "]"));
            checkToken(scanner, new Token(Token.Type.BracesLeft, new Position(7,30), "{"));
            checkToken(scanner, new Token(Token.Type.ColorLiteral, new Position(7,31), "Red"));
            checkToken(scanner, new Token(Token.Type.Comma, new Position(7,34), ","));
            checkToken(scanner, new Token(Token.Type.ColorLiteral, new Position(7,36), "Blue"));
            checkToken(scanner, new Token(Token.Type.BracesRight, new Position(7,40), "}"));
            checkToken(scanner, new Token(Token.Type.Semicolon, new Position(7,41), ";"));
            checkToken(scanner, new Token(Token.Type.If, new Position(8,2), "if"));
            checkToken(scanner, new Token(Token.Type.ParenthesisLeft, new Position(8,4), "("));
            checkToken(scanner, new Token(Token.Type.Planet, new Position(8,6), "planet"));
            checkToken(scanner, new Token(Token.Type.ParenthesisLeft, new Position(8,12), "("));
            checkToken(scanner, new Token(Token.Type.PlanetLiteral, new Position(8,13), "p4"));
            checkToken(scanner, new Token(Token.Type.ParenthesisRight, new Position(8,15), ")"));
            checkToken(scanner, new Token(Token.Type.Has, new Position(8,17), "has"));
            checkToken(scanner, new Token(Token.Type.ParenthesisLeft, new Position(8,20), "("));
            checkToken(scanner, new Token(Token.Type.UnitLiteral, new Position(8,21), "Carrier"));
            checkToken(scanner, new Token(Token.Type.ParenthesisRight, new Position(8,28), ")"));
            checkToken(scanner, new Token(Token.Type.ParenthesisRight, new Position(8,30), ")"));
            checkToken(scanner, new Token(Token.Type.BracesLeft, new Position(8,31), "{"));
            checkToken(scanner, new Token(Token.Type.Foreach, new Position(9,3), "foreach"));
            checkToken(scanner, new Token(Token.Type.ParenthesisLeft, new Position(9,10), "("));
            checkToken(scanner, new Token(Token.Type.VarType, new Position(9,11), "var"));
            checkToken(scanner, new Token(Token.Type.Identifier, new Position(9,15), "p"));
            checkToken(scanner, new Token(Token.Type.Colon, new Position(9,16), ":"));
            checkToken(scanner, new Token(Token.Type.Identifier, new Position(9,17), "players"));
            checkToken(scanner, new Token(Token.Type.ParenthesisRight, new Position(9,24), ")"));
            checkToken(scanner, new Token(Token.Type.BracesLeft, new Position(9,25), "{"));
            checkToken(scanner, new Token(Token.Type.Print, new Position(10,11), "print"));
            checkToken(scanner, new Token(Token.Type.ParenthesisLeft, new Position(10,16), "("));
            checkToken(scanner, new Token(Token.Type.Identifier, new Position(10,17), "p"));
            checkToken(scanner, new Token(Token.Type.ParenthesisRight, new Position(10,18), ")"));
            checkToken(scanner, new Token(Token.Type.Semicolon, new Position(10,19), ";"));
            checkToken(scanner, new Token(Token.Type.BracesRight, new Position(11,9), "}"));
            checkToken(scanner, new Token(Token.Type.BracesRight, new Position(12,2), "}"));
            checkToken(scanner, new Token(Token.Type.Else, new Position(12,3), "else"));
            checkToken(scanner, new Token(Token.Type.BracesLeft, new Position(12,7), "{"));
            checkToken(scanner, new Token(Token.Type.Print, new Position(13,9), "print"));
            checkToken(scanner, new Token(Token.Type.ParenthesisLeft, new Position(13,14), "("));
            checkToken(scanner, new Token(Token.Type.StringLiteral, new Position(13,15), "\"Tak\""));
            checkToken(scanner, new Token(Token.Type.ParenthesisRight, new Position(13,20), ")"));
            checkToken(scanner, new Token(Token.Type.Semicolon, new Position(13,21), ";"));
            checkToken(scanner, new Token(Token.Type.BracesRight, new Position(14,2), "}"));
            checkToken(scanner, new Token(Token.Type.VarType, new Position(15,2), "var"));
            checkToken(scanner, new Token(Token.Type.BracketsLeft, new Position(15,5), "["));
            checkToken(scanner, new Token(Token.Type.BracketsRight, new Position(15,6), "]"));
            checkToken(scanner, new Token(Token.Type.Identifier, new Position(15,8), "units"));
            checkToken(scanner, new Token(Token.Type.Equals, new Position(15,14), "="));
            checkToken(scanner, new Token(Token.Type.Type, new Position(15,16), "unit"));
            checkToken(scanner, new Token(Token.Type.BracketsLeft, new Position(15,20), "["));
            checkToken(scanner, new Token(Token.Type.NumberLiteral, new Position(15,21), "2"));
            checkToken(scanner, new Token(Token.Type.BracketsRight, new Position(15,22), "]"));
            checkToken(scanner, new Token(Token.Type.Semicolon, new Position(15,23), ";"));
            checkToken(scanner, new Token(Token.Type.Type, new Position(16,2), "unit"));
            checkToken(scanner, new Token(Token.Type.BracketsLeft, new Position(16,6), "["));
            checkToken(scanner, new Token(Token.Type.NumberLiteral, new Position(16,7), "0"));
            checkToken(scanner, new Token(Token.Type.BracketsRight, new Position(16,8), "]"));
            checkToken(scanner, new Token(Token.Type.Equals, new Position(16,10), "="));
            checkToken(scanner, new Token(Token.Type.UnitLiteral, new Position(16,12), "Fighter"));
            checkToken(scanner, new Token(Token.Type.Semicolon, new Position(16,19), ";"));
            checkToken(scanner, new Token(Token.Type.Type, new Position(17,2), "unit"));
            checkToken(scanner, new Token(Token.Type.BracketsLeft, new Position(17,6), "["));
            checkToken(scanner, new Token(Token.Type.NumberLiteral, new Position(17,7), "1"));
            checkToken(scanner, new Token(Token.Type.BracketsRight, new Position(17,8), "]"));
            checkToken(scanner, new Token(Token.Type.Equals, new Position(17,10), "="));
            checkToken(scanner, new Token(Token.Type.UnitLiteral, new Position(17,12), "Infantry"));
            checkToken(scanner, new Token(Token.Type.Semicolon, new Position(17,20), ";"));
            checkToken(scanner, new Token(Token.Type.Return, new Position(19,2), "return"));
            checkToken(scanner, new Token(Token.Type.NumberLiteral, new Position(19,9), "0"));
            checkToken(scanner, new Token(Token.Type.Semicolon, new Position(19,10), ";"));
            checkToken(scanner, new Token(Token.Type.BracesRight, new Position(20,1), "}"));
            checkToken(scanner, new Token(Token.Type.EOF, new Position(20,2), "eof"));
        } catch (Exception exception) {
            // TODO: lepsza obsluga wyjatkow
            exception.printStackTrace();
        }
    }
}