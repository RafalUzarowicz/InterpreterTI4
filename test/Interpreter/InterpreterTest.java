package Interpreter;

import Board.Board;
import Interpreter.Environment.Environment;
import Parser.Parser;
import Scanner.Scanner;
import Source.StringSource;
import Utilities.Position;
import Utilities.ProgramTree.Statements.FunctionCall;
import Utilities.ProgramTree.Statements.Print;
import Utilities.ProgramTree.Value.BoardStateCheck.ActivationCheck;
import Utilities.ProgramTree.Value.BoardStateCheck.HexStateCheck;
import Utilities.ProgramTree.Value.BoardStateCheck.PlanetStateCheck;
import Utilities.ProgramTree.Value.BoardStateCheck.PlayerStateCheck;
import Utilities.ProgramTree.Value.FunctionCallValue;
import Utilities.ProgramTree.Value.Literals.*;
import Utilities.ProgramTree.Value.Value;
import Utilities.StringOutStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class InterpreterTest {

    private static Stream<Arguments> provideLiterals() {
        return Stream.of(
                Arguments.of(new BoolLiteral("true"), BoolLiteral.class, "true"),
                Arguments.of(new ColorLiteral("Red"), ColorLiteral.class, "Red"),
                Arguments.of(new HexLiteral("h0"), HexLiteral.class, "h0"),
                Arguments.of(new IntLiteral("1"), IntLiteral.class, "1"),
                Arguments.of(new PlanetLiteral("p0"), PlanetLiteral.class, "p0"),
                Arguments.of(new StringLiteral("text"), StringLiteral.class, "text"),
                Arguments.of(new UnitLiteral("Fighter"), UnitLiteral.class, "Fighter")
        );
    }

    @ParameterizedTest
    @MethodSource("provideLiterals")
    public void literals(Literal literal, Class c, String value) throws Exception {
        // Arrange
        StringSource source = new StringSource("");
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        Interpreter interpreter = new Interpreter(parser.parse(), new Board(), new StringOutStream());
        Environment environment = interpreter.getEnvironment();
        // Act
        literal.accept(interpreter);
        Literal literalPoped = environment.popValue();
        // Assert
        assertEquals(c, literalPoped.getClass());
        assertEquals(value, literalPoped.getValue());
    }



    private static Stream<Arguments> provideStateCheck() {
        return Stream.of(
                Arguments.of(new ActivationCheck(new ColorLiteral("Red"), new HexLiteral("h0")), BoolLiteral.class, "false"),
                Arguments.of(new HexStateCheck(new HexLiteral("h0"), new UnitLiteral("Fighter")), IntLiteral.class, "0"),
                Arguments.of(new PlanetStateCheck(new PlanetLiteral("p0"), new UnitLiteral("Fighter")), IntLiteral.class, "0"),
                Arguments.of(new PlayerStateCheck(new ColorLiteral("Red"), new UnitLiteral("Fighter"), new HexLiteral("h0")), IntLiteral.class, "0")
        );
    }

    @ParameterizedTest
    @MethodSource("provideStateCheck")
    public void activationCheck(Value check, Class c, String value) throws Exception {
        // Arrange
        StringSource source = new StringSource("");
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        Board board = new Board();
        Interpreter interpreter = new Interpreter(parser.parse(), board, new StringOutStream());
        Environment environment = interpreter.getEnvironment();
        // Act
        check.accept(interpreter);
        Literal literalPoped = environment.popValue();
        // Assert
        assertEquals(c, literalPoped.getClass());
        assertEquals(value, literalPoped.getValue());
    }


    @Test
    public void funCallValue() throws Exception {
        // Arrange
        StringSource source = new StringSource("int fun(){return 1;} int main(){ return 0; }");
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        Board board = new Board();
        Interpreter interpreter = new Interpreter(parser.parse(), board, new StringOutStream());
        Environment environment = interpreter.getEnvironment();
        Utilities.ProgramTree.Arguments arguments = new Utilities.ProgramTree.Arguments();
        FunctionCallValue functionCallValue = new FunctionCallValue("fun", arguments);
        // Act
        functionCallValue.accept(interpreter);
        Literal literalPoped = environment.popValue();
        // Assert
        assertEquals(IntLiteral.class, literalPoped.getClass());
        assertEquals("1", literalPoped.getValue());
    }

    @Test
    public void variableValue() throws Exception {
        // Arrange
        StringSource source = new StringSource("int main(){ int x = 5; return x; }");
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        Board board = new Board();
        Interpreter interpreter = new Interpreter(parser.parse(), board, new StringOutStream(false));
        Environment environment = interpreter.getEnvironment();
        // Act
        interpreter.execute();
        Literal literalPoped = environment.popValue();
        // Assert
        assertEquals(IntLiteral.class, literalPoped.getClass());
        assertEquals("5", literalPoped.getValue());
    }

    private static Stream<Arguments> provideExpressions() {
        return Stream.of(
                Arguments.of("2+2*2", "6"),
                Arguments.of("!(2+3)*6--(7+2*3)", "13"),
                Arguments.of("7--3", "10"),
                Arguments.of("(2+2)*2+2", "10"),
                Arguments.of("(2+2)*2", "8"),
                Arguments.of("x + 2", "11"),
                Arguments.of("(2+2)-x", "-5"),
                Arguments.of("12*3+1", "37"),
                Arguments.of("1||1", "true"),
                Arguments.of("1&&0", "false"),
                Arguments.of("1==1", "true"),
                Arguments.of("1==0", "false"),
                Arguments.of("1!=0", "true"),
                Arguments.of("1!=1", "false"),
                Arguments.of("1>0", "true"),
                Arguments.of("0>1", "false"),
                Arguments.of("1>=0", "true"),
                Arguments.of("0>=1", "false"),
                Arguments.of("0<1", "true"),
                Arguments.of("1<0", "false"),
                Arguments.of("0<=1", "true"),
                Arguments.of("1<=0", "false"),
                Arguments.of("!0", "true"),
                Arguments.of("!1", "false"),
                Arguments.of("-1", "-1"),
                Arguments.of("0-1", "-1")
        );
    }

    @ParameterizedTest
    @MethodSource("provideExpressions")
    public void conditionExpression(String expression, String value) throws Exception {
        // Arrange
        StringSource source = new StringSource("int main(){ int x = 9; return "+expression+";}");
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        Interpreter interpreter = new Interpreter(parser.parse(), new Board(), new StringOutStream(false));
        Environment environment = interpreter.getEnvironment();
        // Act
        interpreter.execute();
        Literal literalPoped = environment.popValue();
        // Assert
        assertEquals(value, literalPoped.getValue());
    }

    @Test
    public void loop() throws Exception {
        // Arrange
        StringSource source = new StringSource("int main(){ int[] arr = int[5]{1,2,3,4,5}; int sum = 0; foreach( int x : arr ){ sum = sum + x; } return sum; }");
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        Interpreter interpreter = new Interpreter(parser.parse(), new Board(), new StringOutStream(false));
        Environment environment = interpreter.getEnvironment();
        // Act
        interpreter.execute();
        Literal literalPoped = environment.popValue();
        // Assert
        assertEquals(IntLiteral.class, literalPoped.getClass());
        assertEquals("15", literalPoped.getValue());
    }

    private static Stream<Arguments> provideConditional() {
        return Stream.of(
                Arguments.of("x >= 0", "{ x = 2; }", "", "2"),
                Arguments.of("x > 0", "{ x = 2; }", "", "0"),
                Arguments.of("x >= 0", "{ x = 2; }", "else{ x = 3; }", "2"),
                Arguments.of("x > 0", "{ x = 2; }", "else{ x = 3; }", "3")
        );
    }

    @ParameterizedTest
    @MethodSource("provideConditional")
    public void conditional(String condition, String ifBlock, String elseBlock, String value) throws Exception {
        // Arrange
        StringSource source = new StringSource("int main(){ int x = 0; if( "+condition+" )"+ifBlock+elseBlock+" return x; }");
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        Interpreter interpreter = new Interpreter(parser.parse(), new Board(), new StringOutStream(false));
        Environment environment = interpreter.getEnvironment();
        // Act
        interpreter.execute();
        Literal literalPoped = environment.popValue();
        // Assert
        assertEquals(IntLiteral.class, literalPoped.getClass());
        assertEquals(value, literalPoped.getValue());
    }

    @Test
    public void breakStm() throws Exception {
        // Arrange
        StringSource source = new StringSource("int main(){ int[] arr = int[5]{1,2,3,4,5}; int sum = 0; foreach( int x : arr ){ sum = sum + x; if(x>3){break;}} return sum; }");
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        Interpreter interpreter = new Interpreter(parser.parse(), new Board(), new StringOutStream(false));
        Environment environment = interpreter.getEnvironment();
        // Act
        interpreter.execute();
        Literal literalPoped = environment.popValue();
        // Assert
        assertEquals(IntLiteral.class, literalPoped.getClass());
        assertEquals("10", literalPoped.getValue());
    }

    @Test
    public void continueStm() throws Exception {
        // Arrange
        StringSource source = new StringSource("int main(){ int[] arr = int[5]{1,2,3,4,5}; int sum = 0; foreach( int x : arr ){ if(x<=2){continue;} sum = sum + x;} return sum; }");
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        Interpreter interpreter = new Interpreter(parser.parse(), new Board(), new StringOutStream(false));
        Environment environment = interpreter.getEnvironment();
        // Act
        interpreter.execute();
        Literal literalPoped = environment.popValue();
        // Assert
        assertEquals(IntLiteral.class, literalPoped.getClass());
        assertEquals("12", literalPoped.getValue());
    }

    @Test
    public void printStm() throws Exception {
        // Arrange
        StringSource source = new StringSource("");
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        StringOutStream stringOutStream = new StringOutStream(false);
        Interpreter interpreter = new Interpreter(parser.parse(), new Board(), stringOutStream);
        Print print = new Print(new Position());
        print.add(new IntLiteral("13"));
        // Act
        print.accept(interpreter);
        String outMess = stringOutStream.getStream().toString();
        // Assert
        assertEquals("13", outMess);
    }

    @Test
    public void returnStm() throws Exception {
        // Arrange
        StringSource source = new StringSource("int fun(){ return 0; return 1; } int main(){  return fun(); }");
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        Interpreter interpreter = new Interpreter(parser.parse(), new Board(), new StringOutStream(false));
        Environment environment = interpreter.getEnvironment();
        // Act
        interpreter.execute();
        Literal literalPoped = environment.popValue();
        // Assert
        assertEquals(IntLiteral.class, literalPoped.getClass());
        assertEquals("0", literalPoped.getValue());
    }

    @Test
    public void arrayDeclaration() throws Exception {
        // Arrange
        StringSource source = new StringSource("int main(){ int[] arr = int[3]{1, 2, 3}; int mul = 1; foreach( var x : arr ){ mul = mul*x; } return mul; }");
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        Interpreter interpreter = new Interpreter(parser.parse(), new Board(), new StringOutStream(false));
        Environment environment = interpreter.getEnvironment();
        // Act
        interpreter.execute();
        Literal literalPoped = environment.popValue();
        // Assert
        assertEquals(IntLiteral.class, literalPoped.getClass());
        assertEquals("6", literalPoped.getValue());
    }

    @Test
    public void variableDeclaration() throws Exception {
        // Arrange
        StringSource source = new StringSource("int main(){ int x = 1; int y = x+1; return y; }");
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        Interpreter interpreter = new Interpreter(parser.parse(), new Board(), new StringOutStream(false));
        Environment environment = interpreter.getEnvironment();
        // Act
        interpreter.execute();
        Literal literalPoped = environment.popValue();
        // Assert
        assertEquals(IntLiteral.class, literalPoped.getClass());
        assertEquals("2", literalPoped.getValue());
    }

    @Test
    public void funCallStm() throws Exception {
        // Arrange
        StringSource source = new StringSource("int fun(){player(Red)add(Fighter:5)to(h0); return 0; } int main(){ fun(); return player(Red)has(Fighter)at(h0); }");
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        Board board = new Board();
        Interpreter interpreter = new Interpreter(parser.parse(), board, new StringOutStream(false));
        Environment environment = interpreter.getEnvironment();
        // Act
        interpreter.execute();
        Literal literalPoped = environment.popValue();
        // Assert
        assertEquals(IntLiteral.class, literalPoped.getClass());
        assertEquals("5", literalPoped.getValue());
    }

    @Test
    public void assignment() throws Exception {
        // Arrange
        StringSource source = new StringSource("int main(){ int x = 7; int y = 2; y = x; return y; }");
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        Board board = new Board();
        Interpreter interpreter = new Interpreter(parser.parse(), board, new StringOutStream(false));
        Environment environment = interpreter.getEnvironment();
        // Act
        interpreter.execute();
        Literal literalPoped = environment.popValue();
        // Assert
        assertEquals(IntLiteral.class, literalPoped.getClass());
        assertEquals("7", literalPoped.getValue());
    }

//    // TODO: activate stm
//    @Test
//    public void unitLiteral(){
//        // Arrange
//        // Act
//        // Assert
//    }
//    // TODO: deactivate stm
//    @Test
//    public void unitLiteral(){
//        // Arrange
//        // Act
//        // Assert
//    }
//    // TODO: player action add
//    @Test
//    public void unitLiteral(){
//        // Arrange
//        // Act
//        // Assert
//    }
//    // TODO: player action remove
//    @Test
//    public void unitLiteral(){
//        // Arrange
//        // Act
//        // Assert
//    }
//    // TODO: player action move
//    @Test
//    public void unitLiteral(){
//        // Arrange
//        // Act
//        // Assert
//    }
//
//    // TODO: arguments pass test
//    @Test
//    public void unitLiteral(){
//        // Arrange
//        // Act
//        // Assert
//    }
//    // TODO: return value test
//    @Test
//    public void unitLiteral(){
//        // Arrange
//        // Act
//        // Assert
//    }
//
//    // TODO: no main funciton
//    @Test
//    public void unitLiteral(){
//        // Arrange
//        // Act
//        // Assert
//    }
//    // TODO: main function with arguments
//    @Test
//    public void unitLiteral(){
//        // Arrange
//        // Act
//        // Assert
//    }
//
//    // TODO: 10 simple programs doing something
//    @Test
//    public void unitLiteral(){
//        // Arrange
//        // Act
//        // Assert
//    }
}
