package Scanner;

import Source.Position;
import Source.StringSource;
import Utilities.ScannerUtils;
import Utilities.Token;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 */
class ScannerTest {
    private final static HashMap<String, Token.Type> stringToType = new HashMap<>() {{
        put("player", Token.Type.Player);
        put("planet", Token.Type.Type);
        put("hex", Token.Type.Type);
        put("move", Token.Type.Move);
        put("add", Token.Type.Add);
        put("remove", Token.Type.Remove);
        put("from", Token.Type.From);
        put("to", Token.Type.To);
        put("has", Token.Type.Has);
        put("at", Token.Type.At);
        put("activated", Token.Type.Activated);
        put("activate", Token.Type.Activate);
        put("deactivate", Token.Type.Deactivate);

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

    private void checkTokenNoValue(Scanner scanner, Token token) throws Exception {
        // Act
        Token t = scanner.get();
        // Assert
        assertEquals(token.getType(), t.getType());
        assertEquals(token.getPosition().toString(), t.getPosition().toString());
    }

    private void checkTokenWithValue(Scanner scanner, Token token) throws Exception {
        // Act
        Token t = scanner.get();
        // Assert
        assertEquals(token.getType(), t.getType());
        assertEquals(token.getPosition().toString(), t.getPosition().toString());
        assertEquals(token.getValue(), t.getValue());
    }

    @Test
    void simplest() throws Exception {
        // Arrange
        StringSource source = new StringSource("var x = 2;");
        Scanner scanner = new Scanner(source);
        scanner.next();
        // Act + Assert
        checkTokenWithValue(scanner, new Token(Token.Type.VarType, new Position(1, 1), "var"));
        checkTokenWithValue(scanner, new Token(Token.Type.Identifier, new Position(1, 5), "x"));
        checkTokenNoValue(scanner, new Token(Token.Type.Equals, new Position(1, 7)));
        checkTokenWithValue(scanner, new Token(Token.Type.NumberLiteral, new Position(1, 9), "2"));
        checkTokenNoValue(scanner, new Token(Token.Type.Semicolon, new Position(1, 10)));
    }

    private static Stream<Arguments> provideGoodNumberLiteral() {
        return Stream.of(
                Arguments.of("64152"),
                Arguments.of("62467")
        );
    }

    @ParameterizedTest
    @MethodSource("provideGoodNumberLiteral")
    void goodNumberLiteral(String string) throws Exception {
        // Arrange
        StringSource source = new StringSource(string);
        Scanner scanner = new Scanner(source);
        // Act
        scanner.next();
        Token token = scanner.get();
        // Assert
        assertEquals(token.getType(), Token.Type.NumberLiteral);
        assertEquals(token.getValue(), string);
    }

    private static Stream<Arguments> provideBadNumberLiteral() {
        return Stream.of(
                Arguments.of("0123"),
                Arguments.of("999999999999")
        );
    }

    @ParameterizedTest
    @MethodSource("provideBadNumberLiteral")
    void badNumberLiteral(String string) {
        // Arrange
        StringSource source = new StringSource(string);
        Scanner scanner = new Scanner(source);
        // Act
        // Assert
        assertThrows(Exception.class, scanner::next);
    }

    private static Stream<Arguments> provideBadStringLiteral() {
        return Stream.of(
                Arguments.of("\"tak?\""),
                Arguments.of("\"._.,")
        );
    }

    @ParameterizedTest
    @MethodSource("provideBadStringLiteral")
    void badStringLiteral(String string) {
        // Arrange
        StringSource source = new StringSource(string);
        Scanner scanner = new Scanner(source);
        // Act
        // Assert
        assertThrows(Exception.class, scanner::next);
    }

    private static Stream<Arguments> provideGoodStringLiteral() {
        return Stream.of(
                Arguments.of("\"tak\""),
                Arguments.of("\"._.,\""),
                Arguments.of("\"taki: \"")
        );
    }

    @ParameterizedTest
    @MethodSource("provideGoodStringLiteral")
    void goodStringLiteral(String string) throws Exception {
        // Arrange
        StringSource source = new StringSource(string);
        Scanner scanner = new Scanner(source);
        // Act
        scanner.next();
        Token token = scanner.get();
        // Assert
        assertEquals(token.getType(), Token.Type.StringLiteral);
        assertEquals(token.getValue(), string);
    }

    private static Stream<Arguments> provideBoolLiteral() {
        return Stream.of(
                Arguments.of("true"),
                Arguments.of("false")
        );
    }

    @ParameterizedTest
    @MethodSource("provideBoolLiteral")
    void boolLiteral(String string) throws Exception {
        // Arrange
        StringSource source = new StringSource(string);
        Scanner scanner = new Scanner(source);
        // Act
        scanner.next();
        Token token = scanner.get();
        // Assert
        assertEquals(token.getType(), Token.Type.BoolLiteral);
        assertEquals(token.getValue(), string);
    }

    private static Stream<Arguments> providePlanetLiteral() {
        return Stream.of(
                Arguments.of("p1"),
                Arguments.of("p12"),
                Arguments.of("p123")
        );
    }

    @ParameterizedTest
    @MethodSource("providePlanetLiteral")
    void planetLiteral(String string) throws Exception {
        // Arrange
        StringSource source = new StringSource(string);
        Scanner scanner = new Scanner(source);
        // Act
        scanner.next();
        Token token = scanner.get();
        // Assert
        assertEquals(token.getType(), Token.Type.PlanetLiteral);
        assertEquals(token.getValue(), string);
    }

    private static Stream<Arguments> provideHexLiteral() {
        return Stream.of(
                Arguments.of("h1"),
                Arguments.of("h12"),
                Arguments.of("h123")
        );
    }

    @ParameterizedTest
    @MethodSource("provideHexLiteral")
    void hexLiteral(String string) throws Exception {
        // Arrange
        StringSource source = new StringSource(string);
        Scanner scanner = new Scanner(source);
        // Act
        scanner.next();
        Token token = scanner.get();
        // Assert
        assertEquals(token.getType(), Token.Type.HexLiteral);
        assertEquals(token.getValue(), string);
    }

    private static Stream<Arguments> provideColorBoolAndUnitLiterals() {
        ArrayList<Arguments> list = new ArrayList<>();
        for (String key : ScannerUtils.literalToType.keySet()) {
            list.add(Arguments.of(key, ScannerUtils.literalToType.get(key)));
        }
        return list.stream();
    }

    @ParameterizedTest
    @MethodSource("provideColorBoolAndUnitLiterals")
    void colorAndBoolAndUnitLiteral(String string, Token.Type type) throws Exception {
        // Arrange
        StringSource source = new StringSource(string);
        Scanner scanner = new Scanner(source);
        // Act
        scanner.next();
        Token token = scanner.get();
        // Assert
        assertEquals(token.getType(), type);
        assertEquals(token.getValue(), string);
    }

    private static Stream<Arguments> provideBadIdentifiers() {
        return Stream.of(
                Arguments.of("_"),
                Arguments.of("1_"),
                Arguments.of("_,")
        );
    }

    @ParameterizedTest
    @MethodSource("provideBadIdentifiers")
    void badIdentifiers(String string) {
        // Arrange
        StringSource source = new StringSource(string);
        Scanner scanner = new Scanner(source);
        // Act
        // Assert
        assertThrows(Exception.class, scanner::next);
    }

    private static Stream<Arguments> provideGoodIdentifiers() {
        return Stream.of(
                Arguments.of("x"),
                Arguments.of("_x"),
                Arguments.of("x1")
        );
    }

    @ParameterizedTest
    @MethodSource("provideGoodIdentifiers")
    void goodIdentifiers(String string) throws Exception {
        // Arrange
        StringSource source = new StringSource(string);
        Scanner scanner = new Scanner(source);
        // Act
        scanner.next();
        Token token = scanner.get();
        // Assert
        assertEquals(token.getType(), Token.Type.Identifier);
        assertEquals(token.getValue(), string);
    }

    private static Stream<Arguments> provideAllTokens() {
        ArrayList<Arguments> list = new ArrayList<>();
        for (String key : stringToType.keySet()) {
            list.add(Arguments.of(key, stringToType.get(key)));
        }
        return list.stream();
    }

    @ParameterizedTest
    @MethodSource("provideAllTokens")
    void everyToken(String string, Token.Type type) throws Exception {
        // Arrange
        StringSource source = new StringSource(string);
        Scanner scanner = new Scanner(source);
        // Act
        scanner.next();
        Token token = scanner.get();
        // Assert
        assertEquals(token.getType(), type);
    }

    @Test
    void get() throws Exception {
        // Arrange
        StringSource source = new StringSource("var");
        Scanner scanner = new Scanner(source);
        // Act
        scanner.next();
        Token token = scanner.get();
        // Assert
        assertEquals(token.getType(), Token.Type.VarType);
        assertEquals(token.getPosition().toString(), new Position().toString());
        assertEquals(token.getValue(), "var");
    }

}