package Scanner;

import Source.Position;
import Source.StringSource;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class ScannerTest {
    private void checkTokenNoValue(Scanner scanner, Token token) throws Exception{
        scanner.next();
        Token t = scanner.get();
        assertEquals(token.getType(), t.getType());
        assertEquals(token.getPosition().toString(), t.getPosition().toString());
    }

    private void checkTokenWithValue(Scanner scanner, Token token) throws Exception {
        scanner.next();
        Token t = scanner.get();
        assertEquals(token.getType(), t.getType());
        assertEquals(token.getPosition().toString(), t.getPosition().toString());
        assertEquals(token.getValue(), t.getValue());
    }

    @Test
    void simplest() throws Exception {
        StringSource source = new StringSource("var x = 2;");
        Scanner scanner = new Scanner(source);

        checkTokenWithValue(scanner, new Token(Token.Type.VarType, new Position(1,1), "var"));
        checkTokenWithValue(scanner, new Token(Token.Type.Identifier, new Position(1,5), "x"));
        checkTokenNoValue(scanner, new Token(Token.Type.Equals, new Position(1,7)));
        checkTokenWithValue(scanner, new Token(Token.Type.NumberLiteral, new Position(1,9), "2"));
        checkTokenNoValue(scanner, new Token(Token.Type.Semicolon, new Position(1,10)));
    }

    @Test
    void properNumberLiteral(){
        StringBuilder goodStringBuilder = new StringBuilder();

        goodStringBuilder.append("64152 ");
        goodStringBuilder.append("62467 ");

        String[] goodStrings = goodStringBuilder.toString().split(" ");

        StringSource source = new StringSource(goodStringBuilder.toString());
        Scanner scanner = new Scanner(source);

        for (String string : goodStrings) {
            try {
                scanner.next();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            Token token = scanner.get();
            assertEquals(token.getType(), Token.Type.NumberLiteral);
            assertEquals(token.getValue(), string);
        }

        assertThrows(Exception.class, scanner::next);
        try {
            scanner.next();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        assertThrows(Exception.class, scanner::next);
        try {
            scanner.next();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        try {
            scanner.next();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        Token token = scanner.get();
        assertEquals(token.getType(), Token.Type.EOF);
        assertEquals(token.getValue(), "eof");
    }

    @Test
    void numberLiteral(){
        StringBuilder goodStringBuilder = new StringBuilder();

        goodStringBuilder.append("64152 ");
        goodStringBuilder.append("62467 ");

        String[] goodStrings = goodStringBuilder.toString().split(" ");

        String badString = "0123 " + "999999999999 ";
        StringSource source = new StringSource(goodStringBuilder.toString()+ badString);
        Scanner scanner = new Scanner(source);

        for (String string : goodStrings) {
            try {
                scanner.next();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            Token token = scanner.get();
            assertEquals(token.getType(), Token.Type.NumberLiteral);
            assertEquals(token.getValue(), string);
        }

        assertThrows(Exception.class, scanner::next);
        try {
            scanner.next();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        assertThrows(Exception.class, scanner::next);
        try {
            scanner.next();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        try {
            scanner.next();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        Token token = scanner.get();
        assertEquals(token.getType(), Token.Type.EOF);
        assertEquals(token.getValue(), "eof");
    }

    @Test
    void stringLiteral(){
        StringBuilder goodStringBuilder = new StringBuilder();

        String[] goodStrings = new String[3];
        String tempStr = "\"tak\"";
        goodStringBuilder.append(tempStr).append(" ");
        goodStrings[0] = tempStr;
        tempStr = "\"._.,\"";
        goodStringBuilder.append(tempStr).append(" ");
        goodStrings[1] = tempStr;
        tempStr = "\"taki: \"";
        goodStringBuilder.append(tempStr).append(" ");
        goodStrings[2] = tempStr;


        StringBuilder badStringBuilder = new StringBuilder();

        tempStr = "\"tak?\"";
        badStringBuilder.append(tempStr).append(" ");

        StringSource source = new StringSource(goodStringBuilder.toString()+badStringBuilder.toString());
        Scanner scanner = new Scanner(source);

        for (String string : goodStrings) {
            try {
                scanner.next();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            Token token = scanner.get();
            assertEquals(token.getType(), Token.Type.StringLiteral);
            assertEquals(token.getValue(), string);
        }

        assertThrows(Exception.class, scanner::next);
        assertThrows(Exception.class, scanner::next);
        assertThrows(Exception.class, scanner::next);

        try {
            scanner.next();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        Token token = scanner.get();
        assertEquals(token.getType(), Token.Type.EOF);
        assertEquals(token.getValue(), "eof");
    }

    @Test
    void boolLiteral(){
        StringBuilder goodStringBuilder = new StringBuilder();

        goodStringBuilder.append("true ");
        goodStringBuilder.append("false ");

        String[] goodStrings = goodStringBuilder.toString().split(" ");

        StringBuilder badStringBuilder = new StringBuilder();

        badStringBuilder.append("truew ");
        badStringBuilder.append("fawse ");

        String[] badStrings = badStringBuilder.toString().split(" ");

        StringSource source = new StringSource(goodStringBuilder.toString()+badStringBuilder.toString());
        Scanner scanner = new Scanner(source);

        for (String string : goodStrings) {
            try {
                scanner.next();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            Token token = scanner.get();
            assertEquals(token.getType(), Token.Type.BoolLiteral);
            assertEquals(token.getValue(), string);
        }

        for (String string : badStrings) {
            try {
                scanner.next();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            Token token = scanner.get();
            assertEquals(token.getType(), Token.Type.Identifier);
            assertEquals(token.getValue(), string);
        }

        try {
            scanner.next();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        Token token = scanner.get();
        assertEquals(token.getType(), Token.Type.EOF);
        assertEquals(token.getValue(), "eof");
    }

    @Test
    void planetLiteral(){
        StringBuilder goodStringBuilder = new StringBuilder();

        goodStringBuilder.append("p1 ");
        goodStringBuilder.append("p14 ");
        goodStringBuilder.append("p17 ");

        String[] goodStrings = goodStringBuilder.toString().split(" ");

        StringBuilder badStringBuilder = new StringBuilder();

        badStringBuilder.append("p63 ");
        badStringBuilder.append("p99 ");
        badStringBuilder.append("p59 ");

        String[] badStrings = badStringBuilder.toString().split(" ");

        StringSource source = new StringSource(goodStringBuilder.toString()+badStringBuilder.toString());
        Scanner scanner = new Scanner(source);

        for (String string : goodStrings) {
            try {
                scanner.next();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            Token token = scanner.get();
            assertEquals(token.getType(), Token.Type.PlanetLiteral);
            assertEquals(token.getValue(), string);
        }

        for (String string : badStrings) {
            try {
                scanner.next();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            Token token = scanner.get();
            assertEquals(token.getType(), Token.Type.Identifier);
            assertEquals(token.getValue(), string);
        }

        try {
            scanner.next();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        Token token = scanner.get();
        assertEquals(token.getType(), Token.Type.EOF);
        assertEquals(token.getValue(), "eof");
    }

    @Test
    void hexLiteral(){
        StringBuilder goodStringBuilder = new StringBuilder();

        // TODO: krotsze i oddzielnie dobre i zle
        goodStringBuilder.append("h1 ");
        goodStringBuilder.append("h14 ");
        goodStringBuilder.append("h17 ");

        String[] goodStrings = goodStringBuilder.toString().split(" ");

        StringBuilder badStringBuilder = new StringBuilder();

        badStringBuilder.append("h63 ");
        badStringBuilder.append("h99 ");
        badStringBuilder.append("h51 ");

        String[] badStrings = badStringBuilder.toString().split(" ");

        StringSource source = new StringSource(goodStringBuilder.toString()+badStringBuilder.toString());
        Scanner scanner = new Scanner(source);

        for (String string : goodStrings) {
            try {
                scanner.next();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            Token token = scanner.get();
            assertEquals(token.getType(), Token.Type.HexLiteral);
            assertEquals(token.getValue(), string);
        }

        for (String string : badStrings) {
            try {
                scanner.next();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            Token token = scanner.get();
            assertEquals(token.getType(), Token.Type.Identifier);
            assertEquals(token.getValue(), string);
        }

        try {
            scanner.next();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        Token token = scanner.get();
        assertEquals(token.getType(), Token.Type.EOF);
        assertEquals(token.getValue(), "eof");
    }

    @Test
    void colorAndBoolAndUnitLiteral(){
        for ( String key : Keywords.literalToType.keySet() ) {
            StringSource source = new StringSource(key);
            Scanner scanner = new Scanner(source);
            try {
                scanner.next();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            Token token = scanner.get();

            assertEquals(token.getType(), Keywords.literalToType.get(key));
            assertEquals(token.getPosition().toString(), new Position().toString());
            assertEquals(token.getValue(), key);
        }
    }

    @Test
    void badIdentifiers() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("x_ ");
        stringBuilder.append("x1_ ");
        stringBuilder.append("_x1_ ");

        String[] strings = stringBuilder.toString().split(" ");

        StringSource source = new StringSource(stringBuilder.toString());
        Scanner scanner = new Scanner(source);
        for (String string : strings) {
            try {
                scanner.next();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            Token token = scanner.get();
            assertEquals(token.getType(), Token.Type.Identifier);
            assertEquals(token.getValue(), string.substring(0, string.length()-1));

            assertThrows(Exception.class, scanner::next);
        }

        try {
            scanner.next();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        Token token = scanner.get();
        assertEquals(token.getType(), Token.Type.EOF);
        assertEquals(token.getValue(), "eof");
    }

    @Test
    void goodIdentifiers() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("x ");
        stringBuilder.append("_a ");
        stringBuilder.append("_f1 ");
        stringBuilder.append("____c1 ");
        stringBuilder.append("__xfa ");
        stringBuilder.append("__fwa123 ");

        String[] strings = stringBuilder.toString().split(" ");

        StringSource source = new StringSource(stringBuilder.toString());
        Scanner scanner = new Scanner(source);
        for (String string : strings) {
            try {
                scanner.next();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            Token token = scanner.get();
            assertEquals(token.getType(), Token.Type.Identifier);
            assertEquals(token.getValue(), string);
        }

        try {
            scanner.next();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        Token token = scanner.get();
        assertEquals(token.getType(), Token.Type.EOF);
        assertEquals(token.getValue(), "eof");
    }

    @Test
    void everyTokenSeparately(){
        HashMap<String, Token.Type> stringToType = new HashMap<>(){{
            put("player", Token.Type.Player);
            put("planet", Token.Type.Planet);
            put("hex", Token.Type.Hex);
            put("move", Token.Type.UnitsAction);
            put("add", Token.Type.UnitsAction);
            put("remove", Token.Type.UnitsAction);
            put("from", Token.Type.From);
            put("to", Token.Type.To);
            put("has", Token.Type.Has);
            put("at", Token.Type.At);
            put("activated", Token.Type.Activated);
            put("activate", Token.Type.Activation);
            put("deactivate", Token.Type.Activation);

            put("(", Token.Type.ParenthesisLeft);
            put(")", Token.Type.ParenthesisRight);
            put("[", Token.Type.BracketsLeft);
            put("]", Token.Type.BracketsRight);
            put("{", Token.Type.BracesLeft);
            put("}", Token.Type.BracesRight);

            put(",", Token.Type.Comma);
            put(":", Token.Type.Colon);
            put(";", Token.Type.Semicolon);
            put("-", Token.Type.Minus);
            put("+", Token.Type.Plus);
            put("*", Token.Type.Multiply);
            put("/", Token.Type.Divide);

            put("&&", Token.Type.And);
            put("||", Token.Type.Or);

            put(">", Token.Type.Greater);
            put("<", Token.Type.Less);
            put("=", Token.Type.Equals);
            put("!", Token.Type.Not);

            put(">=", Token.Type.GreaterEqual);
            put("<=", Token.Type.LessEqual);
            put("==", Token.Type.Equal);
            put("!=", Token.Type.NotEqual);

            put("foreach", Token.Type.Foreach);
            put("continue", Token.Type.Continue);
            put("break", Token.Type.Break);
            put("return", Token.Type.Return);
            put("print", Token.Type.Print);
            put("if", Token.Type.If);
            put("else", Token.Type.Else);
        }};

        for ( String key : stringToType.keySet() ) {
            StringSource source = new StringSource(key);
            Scanner scanner = new Scanner(source);
            try {
                scanner.next();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            Token token = scanner.get();

            assertEquals(token.getType(), stringToType.get(key));
            assertEquals(token.getPosition().toString(), new Position().toString());
            assertEquals(token.getValue(), key);
        }
    }

    @Test
    void everyTokenTogether(){
        HashMap<String, Token.Type> stringToType = new HashMap<>(){{
            put("player", Token.Type.Player);
            put("planet", Token.Type.Planet);
            put("hex", Token.Type.Hex);
            put("move", Token.Type.UnitsAction);
            put("add", Token.Type.UnitsAction);
            put("remove", Token.Type.UnitsAction);
            put("from", Token.Type.From);
            put("to", Token.Type.To);
            put("has", Token.Type.Has);
            put("at", Token.Type.At);
            put("activated", Token.Type.Activated);
            put("activate", Token.Type.Activation);
            put("deactivate", Token.Type.Activation);

            put("(", Token.Type.ParenthesisLeft);
            put(")", Token.Type.ParenthesisRight);
            put("[", Token.Type.BracketsLeft);
            put("]", Token.Type.BracketsRight);
            put("{", Token.Type.BracesLeft);
            put("}", Token.Type.BracesRight);

            put(",", Token.Type.Comma);
            put(":", Token.Type.Colon);
            put(";", Token.Type.Semicolon);
            put("-", Token.Type.Minus);
            put("+", Token.Type.Plus);
            put("*", Token.Type.Multiply);
            put("/", Token.Type.Divide);

            put("&&", Token.Type.And);
            put("||", Token.Type.Or);

            put(">", Token.Type.Greater);
            put("<", Token.Type.Less);
            put("=", Token.Type.Equals);
            put("!", Token.Type.Not);

            put(">=", Token.Type.GreaterEqual);
            put("<=", Token.Type.LessEqual);
            put("==", Token.Type.Equal);
            put("!=", Token.Type.NotEqual);

            put("foreach", Token.Type.Foreach);
            put("continue", Token.Type.Continue);
            put("break", Token.Type.Break);
            put("return", Token.Type.Return);
            put("print", Token.Type.Print);
            put("if", Token.Type.If);
            put("else", Token.Type.Else);
        }};
        StringBuilder stringBuilder = new StringBuilder();
        for ( String key : stringToType.keySet() ) {
            stringBuilder.append(key).append(" ");
        }
        String[] strings = stringBuilder.toString().split(" ");
        StringSource source = new StringSource(stringBuilder.toString());
        Scanner scanner = new Scanner(source);
        for (String string : strings) {
            try {
                scanner.next();
            } catch (Exception exception) {
                // TODO: czy mozna
                exception.printStackTrace();
            }
            Token token = scanner.get();
            assertEquals(token.getType(), stringToType.get(string));
            assertEquals(token.getValue(), string);
        }

        try {
            scanner.next();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        Token token = scanner.get();
        assertEquals(token.getType(), Token.Type.EOF);
        assertEquals(token.getValue(), "eof");
    }

    @Test
    void get() {
        StringSource source = new StringSource("var");
        Scanner scanner = new Scanner(source);
        try {
            scanner.next();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        Token token = scanner.get();

        assertEquals(token.getType(), Token.Type.VarType);
        assertEquals(token.getPosition().toString(), new Position().toString());
        assertEquals(token.getValue(), "var");
    }
}