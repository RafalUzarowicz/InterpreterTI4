package Parser;

import Utilities.ProgramTree.ConditionExpresion.*;
import Utilities.ProgramTree.ConditionExpresion.Operators.Operator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ConditionExpressionToStringTest {

    @Test
    void orConditionSingle() {
        // Arrange
        OrCondition orCondition = new OrCondition();
        // Act
        orCondition.add(new Expression());
        // Assert
        assertNotEquals(null, orCondition);
        assertEquals("x", orCondition.toString());
    }

    @Test
    void orConditionMultiple() {
        // Arrange
        OrCondition orCondition = new OrCondition();
        // Act
        orCondition.add(new Expression());
        orCondition.add(new Expression());
        // Assert
        assertNotEquals(null, orCondition);
        assertEquals("(x||x)", orCondition.toString());
    }

    @Test
    void andConditionSingle() {
        // Arrange
        AndCondition andCondition = new AndCondition();
        // Act
        andCondition.add(new Expression());
        // Assert
        assertNotEquals(null, andCondition);
        assertEquals("x", andCondition.toString());
    }

    @Test
    void andConditionMultiple() {
        // Arrange
        AndCondition andCondition = new AndCondition();
        // Act
        andCondition.add(new Expression());
        andCondition.add(new Expression());
        // Assert
        assertNotEquals(null, andCondition);
        assertEquals("(x&&x)", andCondition.toString());
    }

    @Test
    void equalityConditionSingle() {
        // Arrange
        EqualityCondition equalityCondition = new EqualityCondition();
        // Act
        equalityCondition.add(new Expression());
        // Assert
        assertNotEquals(null, equalityCondition);
        assertEquals("x", equalityCondition.toString());
    }

    private static Stream<Arguments> provideEqualityConditionOperator() {
        return Stream.of(
                Arguments.of("=="),
                Arguments.of("!=")
        );
    }

    @ParameterizedTest
    @MethodSource("provideEqualityConditionOperator")
    void equalityConditionMultiple(String operator) {
        // Arrange
        EqualityCondition equalityCondition = new EqualityCondition();
        // Act
        equalityCondition.add(new Expression());
        equalityCondition.add(new Operator(operator), new Expression());
        // Assert
        assertNotEquals(null, equalityCondition);
        assertEquals("(x" + operator + "x)", equalityCondition.toString());
    }

    @Test
    void relationalConditionSingle() {
        // Arrange
        RelationalCondition relationalCondition = new RelationalCondition();
        // Act
        relationalCondition.add(new Expression());
        // Assert
        assertNotEquals(null, relationalCondition);
        assertEquals("x", relationalCondition.toString());
    }

    private static Stream<Arguments> provideRelationalConditionOperator() {
        return Stream.of(
                Arguments.of(">="),
                Arguments.of(">"),
                Arguments.of("<"),
                Arguments.of("<=")
        );
    }

    @ParameterizedTest
    @MethodSource("provideRelationalConditionOperator")
    void relationalConditionMultiple(String operator) {
        // Arrange
        RelationalCondition relationalCondition = new RelationalCondition();
        // Act
        relationalCondition.add(new Expression());
        relationalCondition.add(new Operator(operator), new Expression());
        // Assert
        assertNotEquals(null, relationalCondition);
        assertEquals("(x" + operator + "x)", relationalCondition.toString());
    }


    @Test
    void addExpressionSingle() {
        // Arrange
        AddExpression addExpression = new AddExpression();
        // Act
        addExpression.add(new Expression());
        // Assert
        assertNotEquals(null, addExpression);
        assertEquals("x", addExpression.toString());
    }

    private static Stream<Arguments> provideAddExpressionOperator() {
        return Stream.of(
                Arguments.of("+"),
                Arguments.of("-")
        );
    }

    @ParameterizedTest
    @MethodSource("provideAddExpressionOperator")
    void addExpressionMultiple(String operator) {
        // Arrange
        AddExpression addExpression = new AddExpression();
        // Act
        addExpression.add(new Expression());
        addExpression.add(new Operator(operator), new Expression());
        // Assert
        assertNotEquals(null, addExpression);
        assertEquals("(x" + operator + "x)", addExpression.toString());
    }

    @Test
    void multiplyExpressionSingle() {
        // Arrange
        MultiplyExpression multiplyExpression = new MultiplyExpression();
        // Act
        multiplyExpression.add(new Expression());
        // Assert
        assertNotEquals(null, multiplyExpression);
        assertEquals("x", multiplyExpression.toString());
    }

    private static Stream<Arguments> provideMultiplyExpressionOperator() {
        return Stream.of(
                Arguments.of("*"),
                Arguments.of("/")
        );
    }

    @ParameterizedTest
    @MethodSource("provideMultiplyExpressionOperator")
    void multiplyExpressionMultiple(String operator) {
        // Arrange
        MultiplyExpression multiplyExpression = new MultiplyExpression();
        // Act
        multiplyExpression.add(new Expression());
        multiplyExpression.add(new Operator(operator), new Expression());
        // Assert
        assertNotEquals(null, multiplyExpression);
        assertEquals("(x" + operator + "x)", multiplyExpression.toString());
    }

    @Test
    void unaryExpression() {
        // Arrange
        // Act
        UnaryExpression unaryExpression = new UnaryExpression(new Expression());
        // Assert
        assertNotEquals(null, unaryExpression);
        assertEquals("x", unaryExpression.toString());
    }

    @Test
    void notUnaryExpression() {
        // Arrange
        // Act
        NotUnaryExpression notUnaryExpression = new NotUnaryExpression(new Expression());
        // Assert
        assertNotEquals(null, notUnaryExpression);
        assertEquals("!(x)", notUnaryExpression.toString());
    }

    @Test
    void negativeUnaryExpression() {
        // Arrange
        // Act
        NegativeUnaryExpression negativeUnaryExpression = new NegativeUnaryExpression(new Expression());
        // Assert
        assertNotEquals(null, negativeUnaryExpression);
        assertEquals("-(x)", negativeUnaryExpression.toString());
    }
}
