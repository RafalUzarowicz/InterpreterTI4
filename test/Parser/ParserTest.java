package Parser;

import Exceptions.ParserException;
import Utilities.ProgramTree.*;
import Utilities.ProgramTree.BoardChange.*;
import Utilities.ProgramTree.ConditionExpresion.Expression;
import Utilities.ProgramTree.Statements.*;
import Utilities.ProgramTree.Value.BoardStateCheck.ActivationCheck;
import Utilities.ProgramTree.Value.BoardStateCheck.HexStateCheck;
import Utilities.ProgramTree.Value.BoardStateCheck.PlanetStateCheck;
import Utilities.ProgramTree.Value.BoardStateCheck.PlayerStateCheck;
import Utilities.ProgramTree.Value.FunctionCallValue;
import Utilities.ProgramTree.Value.Literals.*;
import Utilities.ProgramTree.Value.Value;
import Utilities.ProgramTree.Variables.IntVariable;
import Scanner.Scanner;
import Utilities.Token;
import Utilities.Position;
import Source.StringSource;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {

    private static Stream<Arguments> providePlayerStateCheckGood() {
        return Stream.of(
                Arguments.of("Red", "Carrier", "h0"),
                Arguments.of("Green", "Fighter", "h1"),
                Arguments.of("Blue", "Destroyer", "h2")
        );
    }

    @ParameterizedTest
    @MethodSource("providePlayerStateCheckGood")
    void goodPlayerStateCheck(String player, String unit, String place) throws Exception {
        // Arrange
        String line = "player("+player+")has("+unit+")at("+place+")";
        StringSource source = new StringSource(line);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        // Act
        PlayerStateCheck playerStateCheck = (PlayerStateCheck) parser.tryPlayerStateCheckOrActivationCheck();
        // Assert
        assertEquals(ColorLiteral.class, playerStateCheck.getPlayer().getClass());
        assertEquals(player, ((Literal) playerStateCheck.getPlayer()).getValue());
        assertEquals(UnitLiteral.class, playerStateCheck.getUnit().getClass());
        assertEquals(unit, ((Literal) playerStateCheck.getUnit()).getValue());
        assertEquals(HexLiteral.class, playerStateCheck.getPlace().getClass());
        assertEquals(place, ((Literal) playerStateCheck.getPlace()).getValue());
    }

    private static Stream<Arguments> providePlayerStateCheckBad() {
        return Stream.of(
                Arguments.of("player;Red)has(Fighter)at(h1)"),
                Arguments.of("player)Red)has(Fighter)at(h1)"),
                Arguments.of("player(Red)has:Fighter)at(h1)"),
                Arguments.of("player(Red)has(Fighter)at)h1)")
        );
    }

    @ParameterizedTest
    @MethodSource("providePlayerStateCheckBad")
    void badPlayerStateCheck(String string) throws Exception {
        // Arrange
        StringSource source = new StringSource(string);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        // Act
        // Assert
        assertThrows(ParserException.class, parser::tryPlayerStateCheckOrActivationCheck);
    }


    private static Stream<Arguments> provideActivationCheckGood() {
        return Stream.of(
                Arguments.of("Red", "h0"),
                Arguments.of("Green", "h1"),
                Arguments.of("Blue", "h2")
        );
    }

    @ParameterizedTest
    @MethodSource("provideActivationCheckGood")
    void goodActivationCheck(String player, String place) throws Exception {
        // Arrange
        String line = "player("+player+")activated("+place+")";
        StringSource source = new StringSource(line);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        // Act
        ActivationCheck activationCheck = (ActivationCheck) parser.tryPlayerStateCheckOrActivationCheck();
        // Assert
        assertEquals(ColorLiteral.class, activationCheck.getPlayer().getClass());
        assertEquals(player, ((Literal) activationCheck.getPlayer()).getValue());
        assertEquals(HexLiteral.class, activationCheck.getHex().getClass());
        assertEquals(place, ((Literal) activationCheck.getHex()).getValue());
    }

    private static Stream<Arguments> provideActivationCheckBad() {
        return Stream.of(
                Arguments.of("player;Red)activated(h1)"),
                Arguments.of("player)Red(activated(h1)"),
                Arguments.of("player(Red)activated)h1)"),
                Arguments.of("player(Red)activated)h1(")
        );
    }

    @ParameterizedTest
    @MethodSource("provideActivationCheckBad")
    void badActivationCheck(String string) throws Exception {
        // Arrange
        StringSource source = new StringSource(string);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        // Act
        // Assert
        assertThrows(ParserException.class, parser::tryPlayerStateCheckOrActivationCheck);
    }


    private static Stream<Arguments> provideHexStateCheckGood() {
        return Stream.of(
                Arguments.of("hex", "h0", "Fighter"),
                Arguments.of("hex", "h0", "Carrier")
        );
    }

    @ParameterizedTest
    @MethodSource("provideHexStateCheckGood")
    void goodHexStateCheck(String hex, String place, String unit) throws Exception {
        // Arrange
        String line = hex+"("+place+")has("+unit+")";
        StringSource source = new StringSource(line);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        // Act
        HexStateCheck hexStateCheck = (HexStateCheck) parser.tryPlanetOrHexStateCheck();
        // Assert
        assertEquals(UnitLiteral.class, hexStateCheck.getUnit().getClass());
        assertEquals(unit, ((Literal) hexStateCheck.getUnit()).getValue());
        assertEquals(HexLiteral.class, hexStateCheck.getHex().getClass());
        assertEquals(place, ((Literal) hexStateCheck.getHex()).getValue());
    }

    private static Stream<Arguments> provideHexStateCheckBad() {
        return Stream.of(
                Arguments.of("hex)h0)has(Fighter)"),
                Arguments.of("hex(h0;has(Fighter)"),
                Arguments.of("hex(h0)has)Fighter)"),
                Arguments.of("hex(h0)has(Fighter("),
                Arguments.of("hex(h0)activated(Fighter)")
        );
    }

    @ParameterizedTest
    @MethodSource("provideHexStateCheckBad")
    void badHexStateCheck(String string) throws Exception {
        // Arrange
        StringSource source = new StringSource(string);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        // Act
        // Assert
        assertThrows(ParserException.class, parser::tryPlanetOrHexStateCheck);
    }


    private static Stream<Arguments> providePlanetStateCheckGood() {
        return Stream.of(
                Arguments.of("planet", "p0", "Fighter"),
                Arguments.of("planet", "p0", "Carrier")
        );
    }

    @ParameterizedTest
    @MethodSource("providePlanetStateCheckGood")
    void goodPlanetStateCheck(String planet, String place, String unit) throws Exception {
        // Arrange
        String line = planet+"("+place+")has("+unit+")";
        StringSource source = new StringSource(line);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        // Act
        PlanetStateCheck planetStateCheck = (PlanetStateCheck) parser.tryPlanetOrHexStateCheck();
        // Assert
        assertEquals(UnitLiteral.class, planetStateCheck.getUnit().getClass());
        assertEquals(unit, ((Literal) planetStateCheck.getUnit()).getValue());
        assertEquals(PlanetLiteral.class, planetStateCheck.getPlanet().getClass());
        assertEquals(place, ((Literal) planetStateCheck.getPlanet()).getValue());
    }

    private static Stream<Arguments> providePlanetStateCheckBad() {
        return Stream.of(
                Arguments.of("planet)h0)has(Fighter)"),
                Arguments.of("planet(h0;has(Fighter)"),
                Arguments.of("planet(h0)has)Fighter)"),
                Arguments.of("planet(h0)has(Fighter("),
                Arguments.of("planet(h0)activated(Fighter)")
        );
    }

    @ParameterizedTest
    @MethodSource("providePlanetStateCheckBad")
    void badPlanetStateCheck(String string) throws Exception {
        // Arrange
        StringSource source = new StringSource(string);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        // Act
        // Assert
        assertThrows(ParserException.class, parser::tryPlanetOrHexStateCheck);
    }


    private static Stream<Arguments> provideBoardStateCheckGood() {
        return Stream.of(
                Arguments.of("planet(h0)has(Fighter)"),
                Arguments.of("hex(h0)has(Fighter)"),
                Arguments.of("player(Red)activated(h1)"),
                Arguments.of("player(Red)has(Fighter)at(h1)")
        );
    }

    @ParameterizedTest
    @MethodSource("provideBoardStateCheckGood")
    void goodBoardStateCheck(String line) throws Exception {
        // Arrange
        StringSource source = new StringSource(line);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        // Act
        Value planetStateCheck = parser.tryBoardStateCheck();
        // Assert
        assertNotEquals(null, planetStateCheck);
    }

    @ParameterizedTest
    @MethodSource({"provideHexStateCheckBad", "providePlanetStateCheckBad", "providePlayerStateCheckBad", "providePlayerStateCheckBad"})
    void badBoardStateCheck(String string) throws Exception {
        // Arrange
        StringSource source = new StringSource(string);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        // Act
        // Assert
        assertThrows(ParserException.class, parser::tryBoardStateCheck);
    }


    private static Stream<Arguments> provideFunctionCallValueGood() {
        return Stream.of(
                Arguments.of(new Token(Token.Type.Identifier, new Position(), "id"), "x,2,5"),
                Arguments.of(new Token(Token.Type.Identifier, new Position(), "id"), ""),
                Arguments.of(new Token(Token.Type.Identifier, new Position(), "id"), "x(),2")
        );
    }

    @ParameterizedTest
    @MethodSource("provideFunctionCallValueGood")
    void goodFunctionCallValue(Token token, String argumentsString) throws Exception {
        // Arrange
        String line = token.getValue()+"("+argumentsString+")";
        StringSource source = new StringSource(line);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        // Act
        scanner.next();
        FunctionCallValue functionCallValue = parser.tryFunctionCallValue(token);
        var arguments = functionCallValue.getArguments();
        String[] args = argumentsString.split(",");
        // Assert
        assertNotEquals(null, arguments);
        assertEquals(token.getValue(), functionCallValue.getIdentifier());
        if(!argumentsString.equals("")){
            assertEquals(args.length, arguments.getArguments().size());
        }else{
            assertEquals(0, arguments.getArguments().size());
        }
    }

    private static Stream<Arguments> provideFunctionCallValueBad() {
        return Stream.of(
                Arguments.of(new Token(Token.Type.Identifier, new Position(), "fun"), ")x)"),
                Arguments.of(new Token(Token.Type.Identifier, new Position(), "fun"), "(x("),
                Arguments.of(new Token(Token.Type.Identifier, new Position(), "fun"), "(activated)")
        );
    }

    @ParameterizedTest
    @MethodSource("provideFunctionCallValueBad")
    void badFunctionCallValue(Token token, String string) throws Exception {
        // Arrange
        String line = token.getValue()+string;
        StringSource source = new StringSource(line);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        // Act
        scanner.next();
        // Assert
        assertThrows(ParserException.class, () -> parser.tryFunctionCallValue(token));
    }


    private static Stream<Arguments> provideValueGood() {
        return Stream.of(
                Arguments.of("12"),
                Arguments.of("h0"),
                Arguments.of("p1"),
                Arguments.of("Red"),
                Arguments.of("Fighter"),
                Arguments.of("\"Tak\""),
                Arguments.of("true"),
                Arguments.of("x"),
                Arguments.of("arr[2]"),
                Arguments.of("fun()"),
                Arguments.of("player(Red)activated(h0)"),
                Arguments.of("hex(h1)has(Carrier)")
        );
    }

    @ParameterizedTest
    @MethodSource("provideValueGood")
    void goodValue(String line) throws Exception {
        // Arrange
        StringSource source = new StringSource(line);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        // Act
        Value value = parser.tryValue();
        // Assert
        assertNotEquals(null, value);
    }

    private static Stream<Arguments> provideValueBad() {
        return Stream.of(
                Arguments.of("int x;"),
                Arguments.of("if"),
                Arguments.of("foreach"),
                Arguments.of(","),
                Arguments.of(";"),
                Arguments.of("var")
        );
    }

    @ParameterizedTest
    @MethodSource("provideValueBad")
    void badValue(String line) throws Exception {
        // Arrange
        StringSource source = new StringSource(line);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        // Act
        Value value = parser.tryValue();
        // Assert
        assertNull(value);
    }


    private static Stream<Arguments> provideConditionExpressionGood() {
        return Stream.of(
                Arguments.of("12", "12"),
                Arguments.of("h0", "h0"),
                Arguments.of("p1", "p1"),
                Arguments.of("Red", "Red"),
                Arguments.of("Fighter", "Fighter"),
                Arguments.of("\"Tak\"", "\"Tak\""),
                Arguments.of("true", "true"),
                Arguments.of("x", "x"),
                Arguments.of("arr[2]", "arr[2]"),
                Arguments.of("fun()", "fun()"),
                Arguments.of("player(Red)activated(h0)", "player(Red)activated(h0)"),
                Arguments.of("hex(h1)has(Carrier)", "hex(h1)has(Carrier)"),
                Arguments.of("7*2+3", "((7*2)+3)"),
                Arguments.of("(3+4)*(8-1||2)", "((3+4)*((8-1)||2))"),
                Arguments.of("2&&7----1", "(2&&(7--(-(-(1)))))"),
                Arguments.of("!(x||!y+2)", "!((x||(!(y)+2)))"),
                Arguments.of("2+2*2", "(2+(2*2))")
        );
    }

    @ParameterizedTest
    @MethodSource("provideConditionExpressionGood")
    void goodConditionExpression(String line, String expected) throws Exception {
        // Arrange
        StringSource source = new StringSource(line);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        // Act
        Expression orCondition = parser.tryOrCondition();
        // Assert
        assertNotEquals(null, orCondition);
        assertEquals(expected, orCondition.toString());
    }


    private static Stream<Arguments> provideConditionExpressionBad() {
        return Stream.of(
                Arguments.of("7*2++3"),
                Arguments.of("(3+4)*(8-1||2("),
                Arguments.of("!(x+(!y+2)"),
                Arguments.of("!(x+(!y+2)")
                );
    }

    @ParameterizedTest
    @MethodSource("provideConditionExpressionBad")
    void badConditionExpression(String line) throws Exception {
        // Arrange
        StringSource source = new StringSource(line);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        // Act
        // Assert
        assertThrows(ParserException.class, parser::tryOrCondition);
    }


    private static Stream<Arguments> provideArrayDeclarationGood() {
        return Stream.of(
                Arguments.of("int[] x = int[2];"),
                Arguments.of("int[] x = int[2]{2,5};")
        );
    }

    @ParameterizedTest
    @MethodSource("provideArrayDeclarationGood")
    void goodArrayDeclaration(String line) throws Exception {
        // Arrange
        StringSource source = new StringSource(line);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        // Act
        scanner.next();
        ArrayDeclaration arrayDeclaration = parser.tryArrayDeclaration(scanner.peek());
        // Assert
        assertNotEquals(null, arrayDeclaration);
        assertEquals(IntVariable.class, arrayDeclaration.getType().getVariable().getClass());
        assertEquals("x", arrayDeclaration.getIdentifier());
    }

    private static Stream<Arguments> provideArrayDeclarationBad() {
        return Stream.of(
                Arguments.of("int[] x = int[2[;"),
                Arguments.of("int[] x = int];"),
                Arguments.of("int[] x = int[2]2,5};"),
                Arguments.of("int[] x = int[]{2,5];"),
                Arguments.of("int]] x = int[]{2,5];"),
                Arguments.of("int[[ x = int[]{2,5];"),
                Arguments.of("int[] x = fun[]{2,5];")
        );
    }

    @ParameterizedTest
    @MethodSource("provideArrayDeclarationBad")
    void badArrayDeclaration(String line) throws Exception {
        // Arrange
        StringSource source = new StringSource(line);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        Token id = scanner.get();
        // Act
        // Assert
        assertThrows(ParserException.class, () -> parser.tryArrayDeclaration(id));
    }


    private static Stream<Arguments> provideVariableDeclarationGood() {
        return Stream.of(
                Arguments.of("int x = 2;"),
                Arguments.of("color x = Red;"),
                Arguments.of("unit x = Fighter;"),
                Arguments.of("hex x = h1;")
        );
    }

    @ParameterizedTest
    @MethodSource("provideVariableDeclarationGood")
    void goodVariableDeclaration(String line) throws Exception {
        // Arrange
        StringSource source = new StringSource(line);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        Token type = scanner.get();
        String[] strings = line.split(" ");
        // Act
        VariableDeclaration variableDeclaration = parser.tryVariableDeclaration(type);
        // Assert
        assertNotEquals(null, variableDeclaration);
        assertEquals(parser.keywordToVariable(strings[0], "name").getClass(), variableDeclaration.getVariable().getClass());
        assertEquals(strings[1], variableDeclaration.getVariable().getName());
        assertNotEquals(null, variableDeclaration.getValue());
    }

    private static Stream<Arguments> provideVariableDeclarationBad() {
        return Stream.of(
                Arguments.of("int x ( 2;"),
                Arguments.of("color x ;"),
                Arguments.of("unit x = activated;"),
                Arguments.of("hex x = h1[")
        );
    }

    @ParameterizedTest
    @MethodSource("provideVariableDeclarationBad")
    void badVariableDeclaration(String line) throws Exception {
        // Arrange
        StringSource source = new StringSource(line);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        Token id = scanner.get();
        // Act
        // Assert
        assertThrows(ParserException.class, () -> parser.tryVariableDeclaration(id));
    }


    @ParameterizedTest
    @MethodSource({"provideVariableDeclarationGood", "provideArrayDeclarationGood"})
    void goodVarOrArrayDeclaration(String line) throws Exception {
        // Arrange
        StringSource source = new StringSource(line);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        // Act
        Statement varOrArrayDeclaration = parser.tryVarOrArrayDeclaration();
        // Assert
        assertNotEquals(null, varOrArrayDeclaration);
    }

    @ParameterizedTest
    @MethodSource({"provideVariableDeclarationBad", "provideArrayDeclarationBad"})
    void badVarOrArrayDeclaration(String line) throws Exception {
        // Arrange
        StringSource source = new StringSource(line);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        Token id = scanner.get();
        // Act
        // Assert
        assertThrows(ParserException.class, () -> parser.tryVariableDeclaration(id));
    }


    private static Stream<Arguments> provideAssignGood() {
        return Stream.of(
                Arguments.of("x = 2;"),
                Arguments.of("x = Red;"),
                Arguments.of("x = Fighter;"),
                Arguments.of("x[1] = h1;"),
                Arguments.of("x[4] = h1;"),
                Arguments.of("x[0] = h1;"),
                Arguments.of("x[2] = h1;")
        );
    }

    @ParameterizedTest
    @MethodSource("provideAssignGood")
    void goodAssign(String line) throws Exception {
        // Arrange
        StringSource source = new StringSource(line);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        Token id = scanner.get();
        String[] strings = line.split(" ");
        // Act
        Assignment assignment = parser.tryAssign(id);
        // Assert
        assertNotEquals(null, assignment);
        assertNotEquals(null, assignment.getValue());

        if(strings[0].endsWith("]")){
            String[] subStrs = strings[0].split("\\[");
            assertEquals(subStrs[0], assignment.getIdentifier());
            String subStr = subStrs[1].substring(0, subStrs[1].length()-1);
            assertEquals(subStr, assignment.getIndex());
        }else{
            assertEquals(strings[0], assignment.getIdentifier());
        }
    }

    private static Stream<Arguments> provideAssignBad() {
        return Stream.of(
                Arguments.of("x;"),
                Arguments.of("x = activated;"),
                Arguments.of("x = Fighter"),
                Arguments.of("x[1) = h1;"),
                Arguments.of("x]4] = h1;"),
                Arguments.of("x[0[ = h1;"),
                Arguments.of("x[2];")
        );
    }

    @ParameterizedTest
    @MethodSource("provideAssignBad")
    void badAssign(String line) throws Exception {
        // Arrange
        StringSource source = new StringSource(line);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        Token id = scanner.get();
        // Act
        // Assert
        assertThrows(ParserException.class, () -> parser.tryAssign(id));
    }


    private static Stream<Arguments> provideArgumentsGood() {
        return Stream.of(
                Arguments.of("2,7,4"),
                Arguments.of("x,fun(),3"),
                Arguments.of("2+2,1,3"),
                Arguments.of("true,\"tak\",Red"),
                Arguments.of("x,y,z")
        );
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsGood")
    void goodArguments(String line) throws Exception {
        // Arrange
        StringSource source = new StringSource(line);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        String[] strings = line.split(",");
        // Act
        var arguments = parser.tryArguments();
        // Assert
        assertNotEquals(null, arguments);
        assertEquals(strings.length, arguments.getArguments().size());
    }

    private static Stream<Arguments> provideArgumentsBad() {
        return Stream.of(
                Arguments.of("x2(3")
        );
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsBad")
    void badArguments(String line) throws Exception {
        // Arrange
        StringSource source = new StringSource(line);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        // Act
        // Assert
        assertThrows(ParserException.class, parser::tryArguments);
    }


    private static Stream<Arguments> provideFunctionCallStatementGood() {
        return Stream.of(
                Arguments.of(new Token(Token.Type.Identifier, new Position(), "id"), "x,2,5"),
                Arguments.of(new Token(Token.Type.Identifier, new Position(), "id"), ""),
                Arguments.of(new Token(Token.Type.Identifier, new Position(), "id"), "x(),2,true")
        );
    }

    @ParameterizedTest
    @MethodSource("provideFunctionCallStatementGood")
    void goodFunctionCallStatement(Token token, String argumentsString) throws Exception {
        // Arrange
        String line = token.getValue()+"("+argumentsString+");";
        StringSource source = new StringSource(line);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        // Act
        scanner.next();
        FunctionCall functionCall = parser.tryFunctionCallStatement(token);
        var arguments = functionCall.getArguments();
        String[] args = argumentsString.split(",");
        // Assert
        assertNotEquals(null, arguments);
        assertEquals(token.getValue(), functionCall.getIdentifier());
        if(!argumentsString.equals("")){
            assertEquals(args.length, arguments.getArguments().size());
        }else{
            assertEquals(0, arguments.getArguments().size());
        }
    }

    private static Stream<Arguments> provideFunctionCallStatementBad() {
        return Stream.of(
                Arguments.of(new Token(Token.Type.Identifier, new Position(), "fun"), ")x);"),
                Arguments.of(new Token(Token.Type.Identifier, new Position(), "fun"), "(x(;"),
                Arguments.of(new Token(Token.Type.Identifier, new Position(), "fun"), "(activated);")
        );
    }

    @ParameterizedTest
    @MethodSource("provideFunctionCallStatementBad")
    void badFunctionCallStatement(Token token, String string) throws Exception {
        // Arrange
        String line = token.getValue()+string;
        StringSource source = new StringSource(line);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        // Act
        scanner.next();
        // Assert
        assertThrows(ParserException.class, () -> parser.tryFunctionCallStatement(token));
    }

    private static Stream<Arguments> provideFunctionCallOrAssignGood() {
        return Stream.of(
                Arguments.of("x = 2;"),
                Arguments.of("x = Red;"),
                Arguments.of("x = Fighter;"),
                Arguments.of("x[1] = h1;"),
                Arguments.of("x[4] = h1;"),
                Arguments.of("x[0] = h1;"),
                Arguments.of("x[2] = h1;"),
                Arguments.of("id(x,2,5);"),
                Arguments.of("id();"),
                Arguments.of("id(x, fun(), 3);")
        );
    }

    @ParameterizedTest
    @MethodSource("provideFunctionCallOrAssignGood")
    void goodFunctionCallOrAssign(String line) throws Exception {
        // Arrange
        StringSource source = new StringSource(line);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        // Act
        Statement functionCallOrAssign = parser.tryFunctionCallOrAssign();
        // Assert
        assertNotEquals(null, functionCallOrAssign);
    }

    private static Stream<Arguments> provideFunctionCallOrAssignBad() {
        return Stream.of(
                Arguments.of("x;"),
                Arguments.of("x = activated;"),
                Arguments.of("x = Fighter"),
                Arguments.of("x[1) = h1;"),
                Arguments.of("x]4] = h1;"),
                Arguments.of("x[0[ = h1;"),
                Arguments.of("x[2];"),
                Arguments.of("id(],2,5);"),
                Arguments.of("id(;"),
                Arguments.of("id(x, fun(), 3[;")
        );
    }

    @ParameterizedTest
    @MethodSource("provideFunctionCallOrAssignBad")
    void badFunctionCallOrAssign(String line) throws Exception {
        // Arrange
        StringSource source = new StringSource(line);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        scanner.next();
        // Act
        Statement functionCallOrAssign = parser.tryFunctionCallOrAssign();
        // Assert
        assertNull(functionCallOrAssign);
    }


    private static Stream<Arguments> provideActivationGood() {
        return Stream.of(
                Arguments.of("activate(h0)"),
                Arguments.of("activate(hexVar)"),
                Arguments.of("deactivate(x)"),
                Arguments.of("deactivate(y)")
        );
    }

    @ParameterizedTest
    @MethodSource("provideActivationGood")
    void goodActivation(String line) throws Exception {
        // Arrange
        StringSource source = new StringSource(line);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        // Act
        Activation activation = parser.tryActivation();
        // Assert
        assertNotEquals(null, activation);
    }

    private static Stream<Arguments> provideActivationBad() {
        return Stream.of(
                Arguments.of("activate)h0)"),
                Arguments.of("activate(hexVar("),
                Arguments.of("deactivate(activated)")
        );
    }

    @ParameterizedTest
    @MethodSource("provideActivationBad")
    void badActivation(String line) throws Exception {
        // Arrange
        StringSource source = new StringSource(line);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        // Act
        // Assert
        assertThrows(ParserException.class, parser::tryActivation);
    }


    private static Stream<Arguments> provideUnitsListGood() {
        return Stream.of(
                Arguments.of("(x:1, Carrier:3)"),
                Arguments.of("()"),
                Arguments.of("(x:1, Carrier:3, fun():call())")
        );
    }

    @ParameterizedTest
    @MethodSource("provideUnitsListGood")
    void goodUnitsList(String line) throws Exception {
        // Arrange
        StringSource source = new StringSource(line);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        // Act
        UnitsList unitsList = parser.tryUnitsList();
        // Assert
        assertNotEquals(null, unitsList);
    }

    private static Stream<Arguments> provideUnitsListBad() {
        return Stream.of(
                Arguments.of("x:1, Carrier:3)"),
                Arguments.of("(x:1, Carrier:3"),
                Arguments.of("])"),
                Arguments.of("(x:1, Carrier:3, fun():call();"),
                Arguments.of("(x:1, Carrier_3, fun():call())"),
                Arguments.of("(x;1, Carrier:3, fun():call();")
        );
    }

    @ParameterizedTest
    @MethodSource("provideUnitsListBad")
    void badUnitsList(String line) throws Exception {
        // Arrange
        StringSource source = new StringSource(line);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        // Act
        // Assert
        assertThrows(ParserException.class, parser::tryUnitsList);
    }


    private static Stream<Arguments> provideUnitsActionGood() {
        return Stream.of(
                Arguments.of("move(x:1, Carrier:3)from(h0)to(x)"),
                Arguments.of("remove()from(p1)"),
                Arguments.of("add(x:1, Carrier:3, fun():call())to(hexVar)")
        );
    }

    @ParameterizedTest
    @MethodSource("provideUnitsActionGood")
    void goodUnitsAction(String line) throws Exception {
        // Arrange
        StringSource source = new StringSource(line);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        // Act
        UnitsAction unitsAction = parser.tryUnitsAction();
        // Assert
        assertNotEquals(null, unitsAction);
    }

    private static Stream<Arguments> provideUnitsActionBad() {
        return Stream.of(
                Arguments.of("move(x:1, Carrier:3)from(h0)to(x"),
                Arguments.of("remove(]from(p1)"),
                Arguments.of("add(x:1, Carrier:3, fun():call()[to(hexVar)")
        );
    }

    @ParameterizedTest
    @MethodSource("provideUnitsActionBad")
    void badUnitsAction(String line) throws Exception {
        // Arrange
        StringSource source = new StringSource(line);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        // Act
        // Assert
        assertThrows(ParserException.class, parser::tryUnitsAction);
    }


    @ParameterizedTest
    @MethodSource({"provideUnitsActionGood", "provideActivationGood"})
    void goodPlayerAction(String line) throws Exception {
        // Arrange
        StringSource source = new StringSource(line);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        // Act
        PlayerAction playerAction = parser.tryPlayerAction();
        // Assert
        assertNotEquals(null, playerAction);
    }

    @ParameterizedTest
    @MethodSource({"provideUnitsActionBad", "provideActivationBad"})
    void badPlayerAction(String line) throws Exception {
        // Arrange
        StringSource source = new StringSource(line);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        // Act
        // Assert
        assertThrows(ParserException.class, parser::tryPlayerAction);
    }


    private static Stream<Arguments> provideBoardChangeGood() {
        return Stream.of(
                Arguments.of("player(Red)move(x:1, Carrier:3)from(h0)to(x);"),
                Arguments.of("player(x)remove()from(p1);"),
                Arguments.of("player(Blue)add(x:1, Carrier:3, fun():call())to(hexVar);"),
                Arguments.of("player(Purple)activate(h0);"),
                Arguments.of("player(Red)activate(hexVar);"),
                Arguments.of("player(Red)deactivate(x);"),
                Arguments.of("player(Red)deactivate(y);")
        );
    }

    @ParameterizedTest
    @MethodSource("provideBoardChangeGood")
    void goodBoardChange(String line) throws Exception {
        // Arrange
        StringSource source = new StringSource(line);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        // Act
        BoardChange boardChange = parser.tryBoardChange();
        // Assert
        assertNotEquals(null, boardChange);
    }

    private static Stream<Arguments> provideBoardChangeBad() {
        return Stream.of(
                Arguments.of("player(Red)move(x:1, Carrier:3)from(h0to(x);"),
                Arguments.of("player(x)remove(;from(p1);"),
                Arguments.of("player(Blue)(x:1, Carrier:3, fun():call())to(hexVar);"),
                Arguments.of("player(activated)activate(h0);"),
                Arguments.of("player(Red)activate]hexVar);"),
                Arguments.of("player]Red)deactivate(x);"),
                Arguments.of("player(Red)deactivate(y)")
        );
    }

    @ParameterizedTest
    @MethodSource("provideBoardChangeBad")
    void badBoardChange(String line) throws Exception {
        // Arrange
        StringSource source = new StringSource(line);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        // Act
        // Assert
        assertThrows(ParserException.class, parser::tryBoardChange);
    }


    private static Stream<Arguments> provideLoopGood() {
        return Stream.of(
                Arguments.of("foreach(int x : array ){ print(2); };"),
                Arguments.of("foreach(int x : array ){ };"),
                Arguments.of("foreach(int x : array ){ print(2); break; };")
        );
    }

    @ParameterizedTest
    @MethodSource("provideLoopGood")
    void goodLoop(String line) throws Exception {
        // Arrange
        StringSource source = new StringSource(line);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        // Act
        Loop loop = parser.tryLoop();
        // Assert
        assertNotEquals(null, loop);
    }

    private static Stream<Arguments> provideLoopBad() {
        return Stream.of(
                Arguments.of("foreach( x : array ){ print(2); };"),
                Arguments.of("foreach(int x ; array ){ };"),
                Arguments.of("foreach(int x : array ({ print(2); break; };")
        );
    }

    @ParameterizedTest
    @MethodSource("provideLoopBad")
    void badLoop(String line) throws Exception {
        // Arrange
        StringSource source = new StringSource(line);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        // Act
        // Assert
        assertThrows(ParserException.class, parser::tryLoop);
    }


    private static Stream<Arguments> provideConditionalGood() {
        return Stream.of(
                Arguments.of("if(x == y ){ print(2); };"),
                Arguments.of("if(x == y ){ print(2); }else{ x=3;};"),
                Arguments.of("if(x == y ){ print(2); }else{};"),
                Arguments.of("if(x == y ){}else{};")
        );
    }

    @ParameterizedTest
    @MethodSource("provideConditionalGood")
    void goodConditional(String line) throws Exception {
        // Arrange
        StringSource source = new StringSource(line);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        // Act
        Conditional conditional = parser.tryConditional();
        // Assert
        assertNotEquals(null, conditional);
    }

    private static Stream<Arguments> provideConditionalBad() {
        return Stream.of(
                Arguments.of("if(x == y ) print(2); }else{ x=3;}"),
                Arguments.of("if(x == y ){ print(2); else{}"),
                Arguments.of("if(x == y ){}else}"),
                Arguments.of("if(x == y ){}else{")
        );
    }

    @ParameterizedTest
    @MethodSource("provideConditionalBad")
    void badConditional(String line) throws Exception {
        // Arrange
        StringSource source = new StringSource(line);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        // Act
        // Assert
        assertThrows(ParserException.class, parser::tryConditional);
    }


    private static Stream<Arguments> provideReturnGood() {
        return Stream.of(
                Arguments.of("return 2;"),
                Arguments.of("return (x+2);"),
                Arguments.of("return x-7;")
        );
    }

    @ParameterizedTest
    @MethodSource("provideReturnGood")
    void goodReturn(String line) throws Exception {
        // Arrange
        StringSource source = new StringSource(line);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        // Act
        Return returnStm = parser.tryReturn();
        // Assert
        assertNotEquals(null, returnStm);
    }

    private static Stream<Arguments> provideReturnBad() {
        return Stream.of(
                Arguments.of("return ;"),
                Arguments.of("return (x+2)")
        );
    }

    @ParameterizedTest
    @MethodSource("provideReturnBad")
    void badReturn(String line) throws Exception {
        // Arrange
        StringSource source = new StringSource(line);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        // Act
        // Assert
        assertThrows(ParserException.class, parser::tryReturn);
    }


    private static Stream<Arguments> providePrintGood() {
        return Stream.of(
                Arguments.of("print(\"Tak\");"),
                Arguments.of("print(\"Tak\", 2, 3);"),
                Arguments.of("print();")
        );
    }

    @ParameterizedTest
    @MethodSource("providePrintGood")
    void goodPrint(String line) throws Exception {
        // Arrange
        StringSource source = new StringSource(line);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        // Act
        Print print = parser.tryPrint();
        // Assert
        assertNotEquals(null, print);
    }

    private static Stream<Arguments> providePrintBad() {
        return Stream.of(
                Arguments.of("print(\"Tak\";"),
                Arguments.of("print\"Tak\", 2, 3);"),
                Arguments.of("print()")
        );
    }

    @ParameterizedTest
    @MethodSource("providePrintBad")
    void badPrint(String line) throws Exception {
        // Arrange
        StringSource source = new StringSource(line);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        // Act
        // Assert
        assertThrows(ParserException.class, parser::tryPrint);
    }


    private static Stream<Arguments> provideContinueGood() {
        return Stream.of(
                Arguments.of("continue;")
        );
    }

    @ParameterizedTest
    @MethodSource("provideContinueGood")
    void goodContinue(String line) throws Exception {
        // Arrange
        StringSource source = new StringSource(line);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        // Act
        Continue continueStm = parser.tryContinue();
        // Assert
        assertNotEquals(null, continueStm);
    }

    private static Stream<Arguments> provideContinueBad() {
        return Stream.of(
                Arguments.of("continue"),
                Arguments.of("continue(")
        );
    }

    @ParameterizedTest
    @MethodSource("provideContinueBad")
    void badContinue(String line) throws Exception {
        // Arrange
        StringSource source = new StringSource(line);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        // Act
        // Assert
        assertThrows(ParserException.class, parser::tryContinue);
    }


    private static Stream<Arguments> provideBreakGood() {
        return Stream.of(
                Arguments.of("break;")
        );
    }

    @ParameterizedTest
    @MethodSource("provideBreakGood")
    void goodBreak(String line) throws Exception {
        // Arrange
        StringSource source = new StringSource(line);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        // Act
        Break breakStm = parser.tryBreak();
        // Assert
        assertNotEquals(null, breakStm);
    }

    private static Stream<Arguments> provideBreakBad() {
        return Stream.of(
                Arguments.of("break"),
                Arguments.of("break(")
        );
    }

    @ParameterizedTest
    @MethodSource("provideBreakBad")
    void badBreak(String line) throws Exception {
        // Arrange
        StringSource source = new StringSource(line);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        // Act
        // Assert
        assertThrows(ParserException.class, parser::tryBreak);
    }


    private static Stream<Arguments> provideStatementGood() {
        return Stream.of(
                Arguments.of("break;"),
                Arguments.of("return 2;"),
                Arguments.of("continue;"),
                Arguments.of("print(2);"),
                Arguments.of("player(Red)move(Carrier:2)from(h0)to(x);"),
                Arguments.of("foreach(int x : array){};"),
                Arguments.of("if(x==2){}else{};"),
                Arguments.of("funcall(2, 3 , 4);"),
                Arguments.of("x=2;"),
                Arguments.of("int x = 2;"),
                Arguments.of("int[] x = int[2];")
        );
    }

    @ParameterizedTest
    @MethodSource("provideStatementGood")
    void goodStatement(String line) throws Exception {
        // Arrange
        StringSource source = new StringSource(line);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        // Act
        Statement statement = parser.tryStatement();
        // Assert
        assertNotEquals(null, statement);
    }

    private static Stream<Arguments> provideStatementBad() {
        return Stream.of(
                Arguments.of("break"),
                Arguments.of("return ;"),
                Arguments.of("continue"),
                Arguments.of("print(2;"),
                Arguments.of("player(Red)move(Carrier:2 from(h0)to(x);"),
                Arguments.of("foreach( x : array){};"),
                Arguments.of("if(x==2){}else};"),
                Arguments.of("funcall(2, 3 , 4)"),
                Arguments.of("x=;"),
                Arguments.of("int x;"),
                Arguments.of("int[] x = int;")
        );
    }

    @ParameterizedTest
    @MethodSource("provideStatementBad")
    void badStatement(String line) throws Exception {
        // Arrange
        StringSource source = new StringSource(line);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        // Act
        // Assert
        assertThrows(ParserException.class, parser::tryStatement);
    }


    private static Stream<Arguments> provideBlockGood() {
        return Stream.of(
                Arguments.of("{}"),
                Arguments.of("{break;}"),
                Arguments.of("{break; continue;}")
        );
    }

    @ParameterizedTest
    @MethodSource("provideBlockGood")
    void goodBlock(String line) throws Exception {
        // Arrange
        StringSource source = new StringSource(line);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        // Act
        Block block = parser.tryBlock();
        // Assert
        assertNotEquals(null, block);
    }

    private static Stream<Arguments> provideBlockBad() {
        return Stream.of(
                Arguments.of("{"),
                Arguments.of("{break;"),
                Arguments.of("break; continue;}")
        );
    }

    @ParameterizedTest
    @MethodSource("provideBlockBad")
    void badBlock(String line) throws Exception {
        // Arrange
        StringSource source = new StringSource(line);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        // Act
        // Assert
        assertThrows(ParserException.class, parser::tryBlock);
    }


    private static Stream<Arguments> provideParametersGood() {
        return Stream.of(
                Arguments.of("int x, color c"),
                Arguments.of("int x"),
                Arguments.of("")
        );
    }

    @ParameterizedTest
    @MethodSource("provideParametersGood")
    void goodParameters(String line) throws Exception {
        // Arrange
        StringSource source = new StringSource(line);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        String[] params = line.split(",");
        // Act
        Parameters parameters = parser.tryParameters();
        // Assert
        assertNotEquals(null, parameters);
        if(!line.equals("")){
            assertEquals(params.length, parameters.getParameters().size());
        }else{
            assertEquals(0, parameters.getParameters().size());
        }
    }

    private static Stream<Arguments> provideParametersBad() {
        return Stream.of(
                Arguments.of("int x, color[ colors"),
                Arguments.of("int 2")
        );
    }

    @ParameterizedTest
    @MethodSource("provideParametersBad")
    void badParameters(String line) throws Exception {
        // Arrange
        StringSource source = new StringSource(line);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        // Act
        // Assert
        assertThrows(ParserException.class, parser::tryParameters);
    }


    private static Stream<Arguments> provideFunctionDefinitionGood() {
        return Stream.of(
                Arguments.of("int x(){}"),
                Arguments.of("int x(int a){}"),
                Arguments.of("int x(int a, int b){}"),
                Arguments.of("int x(){return 2;}")
        );
    }

    @ParameterizedTest
    @MethodSource("provideFunctionDefinitionGood")
    void goodFunctionDefinition(String line) throws Exception {
        // Arrange
        StringSource source = new StringSource(line);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        // Act
        Function function = parser.tryFunctionDefinition();
        // Assert
        assertNotEquals(null, function);
    }

    private static Stream<Arguments> provideFunctionDefinitionBad() {
        return Stream.of(
                Arguments.of("x(){}"),
                Arguments.of("int (int a){}"),
                Arguments.of("int x int a, int[] b){}"),
                Arguments.of("int x({return 2;}")
        );
    }

    @ParameterizedTest
    @MethodSource("provideFunctionDefinitionBad")
    void badFunctionDefinition(String line) throws Exception {
        // Arrange
        StringSource source = new StringSource(line);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        // Act
        // Assert
        assertThrows(ParserException.class, parser::tryFunctionDefinition);
    }


    private static Stream<Arguments> provideProgramGood() {
        return Stream.of(
                Arguments.of("int x(){}\nint main(){}"),
                Arguments.of("int x(int a){}"),
                Arguments.of("int x(int a, int b){}"),
                Arguments.of("int x(){return 2;}")
        );
    }

    @ParameterizedTest
    @MethodSource("provideProgramGood")
    void goodProgram(String line) throws Exception {
        // Arrange
        StringSource source = new StringSource(line);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        // Act
        Program program = parser.parse();
        // Assert
        assertNotEquals(null, program);
    }

    private static Stream<Arguments> provideProgramBad() {
        return Stream.of(
                Arguments.of("x(){}"),
                Arguments.of("int (int a){}"),
                Arguments.of("int x int a, int b){}"),
                Arguments.of("int x({return 2;}")
        );
    }

    @ParameterizedTest
    @MethodSource("provideProgramBad")
    void badProgram(String line) throws Exception {
        // Arrange
        StringSource source = new StringSource(line);
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner);
        // Act
        // Assert
        assertThrows(ParserException.class, parser::parse);
    }
}
