package Interpreter;

import Board.Board;
import Interpreter.Environment.Environment;
import Parser.Parser;
import Scanner.Scanner;
import Source.FileSource;
import Source.StringSource;
import Utilities.Dictionary;
import Utilities.Position;
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

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    private static Stream<Arguments> provideStateCheck() {
        return Stream.of(
                Arguments.of(new ActivationCheck(new ColorLiteral("Red"), new HexLiteral("h0")), BoolLiteral.class, "false"),
                Arguments.of(new HexStateCheck(new HexLiteral("h0"), new UnitLiteral("Fighter")), IntLiteral.class, "0"),
                Arguments.of(new PlanetStateCheck(new PlanetLiteral("p0"), new UnitLiteral("Fighter")), IntLiteral.class, "0"),
                Arguments.of(new PlayerStateCheck(new ColorLiteral("Red"), new UnitLiteral("Fighter"), new HexLiteral("h0")), IntLiteral.class, "0")
        );
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
                Arguments.of("12/3+1", "5"),
                Arguments.of("12*3+1==1==(3-3!=3-1)", "false"),
                Arguments.of("12*3+1&&0&&(3-3!=3-1)", "false"),
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

    private static Stream<Arguments> provideConditional() {
        return Stream.of(
                Arguments.of("x >= 0", "{ x = 2; }", "", "2"),
                Arguments.of("x > 0", "{ x = 2; }", "", "0"),
                Arguments.of("x >= 0", "{ x = 2; }", "else{ x = 3; }", "2"),
                Arguments.of("x > 0", "{ x = 2; }", "else{ x = 3; }", "3")
        );
    }

    private static Stream<Arguments> providePrograms() {
        return Stream.of(
                Arguments.of("./test/programs/program0.txt", "./test/programs/out0.txt"),
                Arguments.of("./test/programs/program1.txt", "./test/programs/out1.txt"),
                Arguments.of("./test/programs/program2.txt", "./test/programs/out2.txt"),
                Arguments.of("./test/programs/program3.txt", "./test/programs/out3.txt"),
                Arguments.of("./test/programs/program4.txt", "./test/programs/out4.txt"),
                Arguments.of("./test/programs/program5.txt", "./test/programs/out5.txt"),
                Arguments.of("./test/programs/program6.txt", "./test/programs/out6.txt")
        );
    }

    private static Stream<Arguments> provideExceptions() {
        return Stream.of(
                Arguments.of("int fun(){} int main(){ return fun(); }"),
                Arguments.of("int fun(){} int main(){ fun(); return 0; }"),
                Arguments.of("int fun(){return 1;} int main(){ return fun2(); }"),
                Arguments.of("int fun(int x){return 1;} int main(){ return fun(); }"),
                Arguments.of("int fun(int x){return 1;} int main(){ fun(); return 0; }"),
                Arguments.of("int main(){ int x = 2; return x[1]; }"),
                Arguments.of("int main(){ int x = 2; return y; }"),
                Arguments.of("int main(){ fun(); return 0; }"),
                Arguments.of("int main(){ int x = player(Red)has(Fighter)at(Blue); return 0; }"),
                Arguments.of("int main(){ int[] x = int[3]{1,2,3,4}; return 0; }"),
                Arguments.of("int main(){ int[] x = int[3]{1,2,3}; foreach(bool b: x){} return 0; }"),
                Arguments.of("int main(){ if(Red > h2){} return 0; }"),
                Arguments.of("int main(){ if(Red >= Fighter){} return 0; }"),
                Arguments.of("int main(){ if(Red < true){} return 0; }"),
                Arguments.of("int main(){ if(Red <= Blue){} return 0; }"),
                Arguments.of("int main(){ player(Red)move(Fighter:1)from(true)to(h0); return 0; }"),
                Arguments.of("int main(){ player(Red)move(Fighter:1)from(h0)to(false); return 0; }")
        );
    }

    @ParameterizedTest
    @MethodSource("provideLiterals")
    public void literals(Literal literal, Class<?> c, String value) throws Exception {
        // Arrange
        StringSource source = new StringSource("");
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        Interpreter interpreter = new Interpreter(parser.parse(), new Board(), new StringOutStream());
        Environment environment = interpreter.getEnvironment();
        // Act
        literal.accept(interpreter);
        Literal literalPopped = environment.popValue();
        // Assert
        assertEquals(c, literalPopped.getClass());
        assertEquals(value, literalPopped.getValue());
    }

    @ParameterizedTest
    @MethodSource("provideStateCheck")
    public void activationCheck(Value check, Class<?> c, String value) throws Exception {
        // Arrange
        StringSource source = new StringSource("");
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        Board board = new Board();
        Interpreter interpreter = new Interpreter(parser.parse(), board, new StringOutStream());
        Environment environment = interpreter.getEnvironment();
        // Act
        check.accept(interpreter);
        Literal literalPopped = environment.popValue();
        // Assert
        assertEquals(c, literalPopped.getClass());
        assertEquals(value, literalPopped.getValue());
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
        Literal literalPopped = environment.popValue();
        // Assert
        assertEquals(IntLiteral.class, literalPopped.getClass());
        assertEquals("1", literalPopped.getValue());
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
        interpreter.execute(false);
        Literal literalPopped = environment.popValue();
        // Assert
        assertEquals(IntLiteral.class, literalPopped.getClass());
        assertEquals("5", literalPopped.getValue());
    }

    @ParameterizedTest
    @MethodSource("provideExpressions")
    public void conditionExpression(String expression, String value) throws Exception {
        // Arrange
        StringSource source = new StringSource("int main(){ int x = 9; return " + expression + ";}");
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        Interpreter interpreter = new Interpreter(parser.parse(), new Board(), new StringOutStream(false));
        Environment environment = interpreter.getEnvironment();
        // Act
        interpreter.execute(false);
        Literal literalPopped = environment.popValue();
        // Assert
        assertEquals(value, literalPopped.getValue());
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
        interpreter.execute(false);
        Literal literalPopped = environment.popValue();
        // Assert
        assertEquals(IntLiteral.class, literalPopped.getClass());
        assertEquals("15", literalPopped.getValue());
    }

    @ParameterizedTest
    @MethodSource("provideConditional")
    public void conditional(String condition, String ifBlock, String elseBlock, String value) throws Exception {
        // Arrange
        StringSource source = new StringSource("int main(){ int x = 0; if( " + condition + " )" + ifBlock + elseBlock + " return x; }");
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        Interpreter interpreter = new Interpreter(parser.parse(), new Board(), new StringOutStream(false));
        Environment environment = interpreter.getEnvironment();
        // Act
        interpreter.execute(false);
        Literal literalPopped = environment.popValue();
        // Assert
        assertEquals(IntLiteral.class, literalPopped.getClass());
        assertEquals(value, literalPopped.getValue());
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
        interpreter.execute(false);
        Literal literalPopped = environment.popValue();
        // Assert
        assertEquals(IntLiteral.class, literalPopped.getClass());
        assertEquals("10", literalPopped.getValue());
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
        interpreter.execute(false);
        Literal literalPopped = environment.popValue();
        // Assert
        assertEquals(IntLiteral.class, literalPopped.getClass());
        assertEquals("12", literalPopped.getValue());
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
        interpreter.execute(false);
        Literal literalPopped = environment.popValue();
        // Assert
        assertEquals(IntLiteral.class, literalPopped.getClass());
        assertEquals("0", literalPopped.getValue());
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
        interpreter.execute(false);
        Literal literalPopped = environment.popValue();
        // Assert
        assertEquals(IntLiteral.class, literalPopped.getClass());
        assertEquals("6", literalPopped.getValue());
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
        interpreter.execute(false);
        Literal literalPopped = environment.popValue();
        // Assert
        assertEquals(IntLiteral.class, literalPopped.getClass());
        assertEquals("2", literalPopped.getValue());
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
        interpreter.execute(false);
        Literal literalPopped = environment.popValue();
        // Assert
        assertEquals(IntLiteral.class, literalPopped.getClass());
        assertEquals("5", literalPopped.getValue());
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
        interpreter.execute(false);
        Literal literalPopped = environment.popValue();
        // Assert
        assertEquals(IntLiteral.class, literalPopped.getClass());
        assertEquals("7", literalPopped.getValue());
    }

    @Test
    public void activation() throws Exception {
        // Arrange
        StringSource source = new StringSource("int main(){ player(Red)activate(h0); return player(Red)activated(h0); }");
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        Board board = new Board();
        Interpreter interpreter = new Interpreter(parser.parse(), board, new StringOutStream(false));
        Environment environment = interpreter.getEnvironment();
        // Act
        interpreter.execute(false);
        Literal literalPopped = environment.popValue();
        // Assert
        assertEquals(BoolLiteral.class, literalPopped.getClass());
        assertEquals("true", literalPopped.getValue());
    }

    @Test
    public void deactivation() throws Exception {
        // Arrange
        StringSource source = new StringSource("int main(){ player(Red)activate(h0); player(Red)deactivate(h0); return player(Red)activated(h0); }");
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        Board board = new Board();
        Interpreter interpreter = new Interpreter(parser.parse(), board, new StringOutStream(false));
        Environment environment = interpreter.getEnvironment();
        // Act
        interpreter.execute(false);
        Literal literalPopped = environment.popValue();
        // Assert
        assertEquals(BoolLiteral.class, literalPopped.getClass());
        assertEquals("false", literalPopped.getValue());
    }

    @Test
    public void add() throws Exception {
        // Arrange
        StringSource source = new StringSource("int main(){ player(Red)add(Fighter:5)to(p0); return player(Red)has(Fighter)at(p0); }");
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        Board board = new Board();
        Interpreter interpreter = new Interpreter(parser.parse(), board, new StringOutStream(false));
        Environment environment = interpreter.getEnvironment();
        // Act
        interpreter.execute(false);
        Literal literalPopped = environment.popValue();
        // Assert
        assertEquals(IntLiteral.class, literalPopped.getClass());
        assertEquals("5", literalPopped.getValue());
        assertEquals(5, board.getPlayerPlanetUnitNumber(0, Dictionary.PlayerColors.Red, Dictionary.LandUnits.Fighter));
    }

    @Test
    public void remove() throws Exception {
        // Arrange
        StringSource source = new StringSource("int main(){ player(Red)remove(Fighter:5)from(p0); return player(Red)has(Fighter)at(p0); }");
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        Board board = new Board();
        Interpreter interpreter = new Interpreter(parser.parse(), board, new StringOutStream(false));
        Environment environment = interpreter.getEnvironment();
        board.changePlayerPlanetUnitNumber(0, Dictionary.PlayerColors.Red, Dictionary.LandUnits.Fighter, 5);
        // Act
        interpreter.execute(false);
        Literal literalPopped = environment.popValue();
        // Assert
        assertEquals(IntLiteral.class, literalPopped.getClass());
        assertEquals("0", literalPopped.getValue());
        assertEquals(0, board.getPlayerPlanetUnitNumber(0, Dictionary.PlayerColors.Red, Dictionary.LandUnits.Fighter));
    }

    @Test
    public void move() throws Exception {
        // Arrange
        StringSource source = new StringSource("int main(){ player(Red)move(Fighter:5)from(p1)to(p0); return player(Red)has(Fighter)at(p0); }");
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        Board board = new Board();
        Interpreter interpreter = new Interpreter(parser.parse(), board, new StringOutStream(false));
        Environment environment = interpreter.getEnvironment();
        board.changePlayerPlanetUnitNumber(1, Dictionary.PlayerColors.Red, Dictionary.LandUnits.Fighter, 5);
        // Act
        interpreter.execute(false);
        Literal literalPopped = environment.popValue();
        // Assert
        assertEquals(IntLiteral.class, literalPopped.getClass());
        assertEquals("5", literalPopped.getValue());
        assertEquals(5, board.getPlayerPlanetUnitNumber(0, Dictionary.PlayerColors.Red, Dictionary.LandUnits.Fighter));
        assertEquals(0, board.getPlayerPlanetUnitNumber(1, Dictionary.PlayerColors.Red, Dictionary.LandUnits.Fighter));
    }

    @Test
    public void argument() throws Exception {
        // Arrange
        StringSource source = new StringSource("int fun(int x, int y){return x+y; } int main(){ return fun(2, 3); }");
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        Board board = new Board();
        Interpreter interpreter = new Interpreter(parser.parse(), board, new StringOutStream(false));
        Environment environment = interpreter.getEnvironment();
        // Act
        interpreter.execute(false);
        Literal literalPopped = environment.popValue();
        // Assert
        assertEquals(IntLiteral.class, literalPopped.getClass());
        assertEquals("5", literalPopped.getValue());
    }

    @Test
    public void returnValue() throws Exception {
        // Arrange
        StringSource source = new StringSource("int fun(int x, int y){return x+y*2 || 0; } int main(){ bool b = fun(2,3); return b; }");
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        Board board = new Board();
        Interpreter interpreter = new Interpreter(parser.parse(), board, new StringOutStream(false));
        Environment environment = interpreter.getEnvironment();
        // Act
        interpreter.execute(false);
        Literal literalPopped = environment.popValue();
        // Assert
        assertEquals(BoolLiteral.class, literalPopped.getClass());
        assertEquals("true", literalPopped.getValue());
    }

    @Test
    public void noMain() throws Exception {
        // Arrange
        StringSource source = new StringSource("int fun(int x, int y){return x+y*2 || 0; }");
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        Board board = new Board();
        Interpreter interpreter = new Interpreter(parser.parse(), board, new StringOutStream(false));
        // Act
        // Assert
        assertThrows(Exception.class, interpreter::execute);
    }

    @Test
    public void mainWithArguments() throws Exception {
        // Arrange
        StringSource source = new StringSource("int main(int x, int y){return x+y*2 || 0; }");
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        Board board = new Board();
        Interpreter interpreter = new Interpreter(parser.parse(), board, new StringOutStream(false));
        // Act
        // Assert
        assertThrows(Exception.class, () -> interpreter.execute(false));
    }

    @ParameterizedTest
    @MethodSource("providePrograms")
    public void execute(String programFile, String outFilePath) throws Exception {
        // Arrange
        Path outFile = Path.of(outFilePath);
        String outPrint = Files.readString(outFile);
        FileSource source = new FileSource(programFile);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        Board board = new Board();
        StringOutStream stringOutStream = new StringOutStream(false);
        Interpreter interpreter = new Interpreter(parser.parse(), board, stringOutStream);
        // Act
        interpreter.execute(false);
        // Assert
        assertEquals(outPrint, stringOutStream.getStream().toString());
    }

    @ParameterizedTest
    @MethodSource("provideExceptions")
    public void exceptions(String program) throws Exception {
        // Arrange
        StringSource source = new StringSource(program);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        Board board = new Board();
        Interpreter interpreter = new Interpreter(parser.parse(), board, new StringOutStream(false));
        // Act
        // Assert
        assertThrows(Exception.class, () -> interpreter.execute(false));
    }
}
